package cn.zgc.seckill;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置Spring和junit的整合（spring-test加junit），junit启动时加载SpringIOC容器
 * @author zgc
 */

@RunWith(SpringJUnit4ClassRunner.class)
//指定Spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class BaseTest {

}
