package com.example.wenda;

import com.example.wenda.dao.QuestionDAO;
import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.Question;
import com.example.wenda.model.User;
import com.example.wenda.service.FollowService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatebaseTests {

	@Autowired
	UserDAO userDAO;

	@Autowired
	QuestionDAO questionDAO;

	@Autowired
	FollowService followService;

	@Test
	public void initDatebase() {
		Random random = new Random();

		for(int i=0;i<11;i++){
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			for(int j=1;j<i;j++){
				followService.follow(j, EntityType.ENTITY_USER,i);
			}

			user.setPassword("xx");
			userDAO.updatePassWord(user);

			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*i);
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("TITLE{%d}",i));
			question.setContent(String.format("Balalallalalalal Content %d",i));

			questionDAO.addQuestion(question);
		}

		Assert.assertEquals("xx",userDAO.selectById(1).getPassword());
		userDAO.deleteById(1);
//		Assert.assertNull(userDAO);
	}

}
