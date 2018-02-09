package ws.sample.endpoint;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

import jndi.sample.MybatisSessionFactory;
import jndi.sample.dao.ItemDao;
import jndi.sample.entity.Item;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/echo")
public class Echo {

    private static final Logger logger = LoggerFactory.getLogger(Echo.class);

    @OnOpen
    public void onOpen(Session session) {
        logger.info("{} has opened a connection.", session.getId());
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @OnClose
    public void onClose(Session session) {
        logger.warn("Session {} has ended.", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Message from {}: {}", session.getId() , message);
        try {
            SqlSessionFactory factory = MybatisSessionFactory.getSqlSessionFactory();
            SqlSession sqlSession = factory.openSession();
            ItemDao itemDao = sqlSession.getMapper(ItemDao.class);
            Item item = itemDao.getItemById(Long.parseLong(message));
            session.getBasicRemote().sendText(item.getName());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @OnError
    public void onError(Throwable e) {
        logger.error(e.getMessage(), e);
    }

}