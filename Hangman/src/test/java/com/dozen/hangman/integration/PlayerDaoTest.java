package com.dozen.hangman.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dozen.hangman.HangmanApplication;
import com.dozen.hangman.dao.BaseDao;
import com.dozen.hangman.dao.PlayerDao;
import com.dozen.hangman.model.Player;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HangmanApplication.class)
public class PlayerDaoTest extends BaseDao{
	@Autowired
	PlayerDao playerDao;
	
	static String PLA_NAME = "pla-name";

	@Test
	@Transactional
	public void whenFindByName_thenReturnEmployee() {
	    // given
	    Player pla = new Player();
	    pla.setName(PLA_NAME);
	    pla.setAge(34);
	    getCurrentSession().persist(pla);
	    getCurrentSession().flush();
	 
	    // when
	    
	    Player found = playerDao.getPlayerByName(pla.getName());
	 
	    // then
	    assertThat(found.getName())
	      .isEqualTo(pla.getName());
	}

}
