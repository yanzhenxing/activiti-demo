package com.quauq.yanzhenxing.activiti;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:application-context.xml","classpath:application-context-shiro.xml","classpath:application-context-activiti.xml"})
public class BaseTest {

}
