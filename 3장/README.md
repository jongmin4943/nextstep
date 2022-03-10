#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* 먼저 ServletContextListener 를 상속한 ContextLoaderListener 가 생성되어 contextInitialized 가 실행되어 jwp.sql 스크립트 포함하여 db 커넥션을 맺는다. 그 이후에 HttpServlet 를 상속받은 DispatcherServlet 가 생성되며 init() 메소드가 실행괴며 만들어놓은 RequestMapping 의 initMapping 이 실행 되어 각 Controller 들을 생성해 map 에 넣는다.
<br/>
<br/>
* 서블릿 컨테이너는 웹 어플리케이션의 상태를 관리하는 ServletContext 를 생성한다. 생성하며 ServletContextListener 의  contextInitialized 가 실행된다. 그 후 jwp.sql 을 실행하며 테이블을 초기화한다. @WebServlet 의 loadOnStartup 설정에 의해 서블릿 컨테이너가 실행되는 시점에 인스턴스를 생성한다. 그 후 init() 메소드가 실행되며 RequestMapping 을 통해 각 Controller 들을 맵핑한다. 

#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* 먼저 DispatcherServlet 의 @WebServlet 의 urlPatterns = "/" 이므로 전부 이곳을 통해 실행된다. service() 가 호출되며 생성된 Request 객체를 통해 url 를 추출해 해당 url 을 통해 맵핑된 Controller 객체를 찾는다. / 는 HomeController 가 맵핑 되어 객체가 생성되어 execute() 가 호출된다. 그곳에서 JspView 객체를 생성하며 render() 를 호출한다. 그곳에서 전달받은 model 에 질문 목록을 Request 에 넣어준뒤 RequestDispatcher 의 forward 로 이동시킨다.
  <br/>
  <br/>
* 서블릿에 접근하기 전에 먼저 ResourceFilter 와 CharacterEncodingFilter 의 doFilter() 메소드가 실행된다. DispatcherServlet 의 service() 가 실행되어 "/" 와 맵핑된 HomeController 를 생성한다. HomeController 의 execute() 가 실행되어 ModelAndView 를 생성하여 View 는 JspView 가 생성되고 render() 를 실행 응답한다.
#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* Controller 나 Dao 같은 클래스들은 상태를 가지지 않기때문에 (필드 변수가 없다) 멀티 스레드 환경에서 막 쓰여도 상관없다. 하지만 이러한 클래스들이 필드변수를 가지게되면 상태값을 가지게 되는데 그 상태값은 Heap 메모리에 올라가고 상태값이 변할때마다 그 주소값이 바뀌게 된다. 그러므로 멀티스레드 환경에서 실행될때 동시에 실행될시 간헐적으로 후에 실행되어 상태를 변화시킨게 전에 실행된것이 종료되기전에 영향을 끼쳐 동기화 문제가 생길수 있다.
  <br/>
  <br/>
* https://www.youtube.com/watch?v=9lQsAPFQjBg
* 개발자가 갖춰야할 역량 중 하나가 클라이언트 요청마다 매번 인스턴스를 생성해야하는가 재사용해야하는가 판단하는 것이다. 인스턴스가 상태 값을 유지해야하는지에 따라 구분되는데 클라이언트마다 서로 다른 상태값을 가지게 되는데 상태값을 유지 할 필요가 있는 경우에는 매 요청마다 인스턴스를 생성해야한다. 하지만 JdbcTemplate, Dao, Controller 는 매 요청마다 상태값을 가지지 않기때문에 하나를 생성한후 재사용 할수 있다.
<br/>
* 서블릿은 서블릿 컨테이너가 시작할 때 인스턴스 하나를 생성한후 재사용한다. RequestMapping 인스턴스 또한 하나 각 컨트롤러의 인스턴스 또한 하나이다.
* JVM 은 코드를 실행하기 위해 메모리를 스택과 힙 영억으로 나눠서 관리한다. JVM 은 각 메소드 별로 스택 프레임을 생성한다.
  * 스택영역은 각 메소드가 실행될때 메소드의 인자, 로컬 변수등을 관리하는 메모리 영역으로 <strong>'각 스레드마다 서로 다른 스택 영역'</strong>을 가진다.
  * 힙 영역은 클래스의 인스턴스 상태 데이터를 관리하는 영역이다. <strong>힙 영역은 스레드가 서로 공유할 수 있는 영역이다.</strong>
-----------------
