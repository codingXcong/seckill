package cn.zgc.seckill.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.zgc.seckill.dto.Exposer;
import cn.zgc.seckill.dto.SeckillExecution;
import cn.zgc.seckill.dto.SeckillResult;
import cn.zgc.seckill.entity.Seckill;
import cn.zgc.seckill.enums.SeckillStateEnum;
import cn.zgc.seckill.exception.RepeatKillException;
import cn.zgc.seckill.exception.SeckillCloseException;
import cn.zgc.seckill.service.SeckillService;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	
	//列表页
	@RequestMapping("/list")
	public String list(Model model){
		// 获取列表页
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		// list.jsp + model = ModelAndView
		return "list";// WEB-INF/jsp/"list".jsp
	}
	
	//详情页
	@RequestMapping("/{seckillId}/detail")
	public String detail(@PathVariable("seckillId")Long seckillId,Model model){
		if(seckillId == null){
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	// ajax json
		@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {
				"application/json; charset=utf-8" })
		@ResponseBody
		public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
			SeckillResult<Exposer> result;
			try {
				Exposer exposer = seckillService.exportSeckillUrl(seckillId);
				result = new SeckillResult<Exposer>(true, exposer);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result = new SeckillResult<Exposer>(false, e.getMessage());
			}
			return result;
		}

		@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
				"application/json; charset=utf-8" })
		@ResponseBody
		public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
				@PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long phone) {
			// springmvc valid
			if (phone == null) { 
				return new SeckillResult<>(false, "未注册");
			}
			try {
				// 存储过程调用
				//SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
				SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
				return new SeckillResult<SeckillExecution>(true, execution);
			} catch (RepeatKillException e) {
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
				return new SeckillResult<SeckillExecution>(true, execution);
			} catch (SeckillCloseException e) {
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
				return new SeckillResult<SeckillExecution>(true, execution);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
				return new SeckillResult<SeckillExecution>(true, execution);
			}
		}
	
	@ResponseBody
	@RequestMapping(value="/time/now",method=RequestMethod.GET)
	public SeckillResult<Long> time(){
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}
}
