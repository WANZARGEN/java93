package bitcamp.java93.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import bitcamp.java93.dao.MemberDao;
import bitcamp.java93.dao.TeacherDao;
import bitcamp.java93.dao.mysql.MemberDaoImpl;
import bitcamp.java93.dao.mysql.TeacherDaoImpl;
import bitcamp.java93.service.impl.TeacherServiceImpl;
import bitcamp.java93.util.DBConnectionPool;

@WebListener
public class ContextLoaderListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    // 웹 애플리케이션이 시작될 때 DAO를 생성하여 ServletContext 보관소에 저장한다.
    String jdbcDriver = "com.mysql.jdbc.Driver";
    String jdbcUrl = "jdbc:mysql://localhost:3306/webappdb";
    String jdbcUsername = "webapp";
    String jdbcPassword = "1111";
    
    try {
      DBConnectionPool conPool = new DBConnectionPool(
          jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
      MemberDao memberDao = new MemberDaoImpl(conPool);
      TeacherDao teacherDao = new TeacherDaoImpl(conPool);
      
      TeacherServiceImpl teacherService = new TeacherServiceImpl();
      teacherService.setMemberDao(memberDao);
      teacherService.setTeacherDao(teacherDao);
      
      ServletContext sc = sce.getServletContext();
      sc.setAttribute("teacherService", teacherService);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
  }

}
