# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* InputStream 를 Character 로 읽어들이기 위해 BufferedReader 로 변환한다.
* InputStreamReader 를 이용해 InputStream 을 읽어 Reader 객체로 변환 후 BufferedReader 로 변환한다.
* 헤더의 마지막까지 while 문을 이용해 readLine 함수를 이용해 확인한다.
* 들어온 라인 중 시작이 GET 인 경우를 찾는다.
* 이때 정보들은 공백을 이용해 표시되므로 ex) GET /index.html HTTP/1.1
* spilt 함수를 이용하면 0번째는 요청 method, 두번째는 요청 url 이 된다.
* 요청 url 을 추출한다음 Files.readAllBytes 를 이용해 webapp 폴더 안의 요청된 파일을 찾아 읽어
* byte 로 변환해 body 에 넣어준뒤 응답한다.
----------------------------------------
1장에서 배웠든 단일책임원칙으로 나눠보려 했지만 생각보다 쉽지않다. 어디까지 쪼개야하는지 아직 감이 잘 안온다.
일단 내가 생각했을때 이정도면 편하게 다음에도 활용 가능하고 복잡하지 않을정도로만 쪼개보았다.

### 요구사항 2 - get 방식으로 회원가입
* url 에서 요청 url 과 params 를 "?" 를 기준으로 나눈다.
* 요청 url 이 /user/create 일 경우 넘어온 params 를 이용해 User 객체를 생성한다.
----------------------------------------
어떻게 해야 나중에 다시 활용이 가능한 함수를 만들까 고민을 조금 했다. 그래서 나온게 checkPathVariables
함수인데 다 만들고나서 확인해보니 코드가 점점 길어지고 가독성이 떨어지는것같다.
나중에 다른 요청 url 도 실험해봐야겠지만 아직까진 쓸만한 함수라 느껴진다.

### 요구사항 3 - post 방식으로 회원가입
* 헤더에서 Content-length 와 body 의 값을 찾는다.
* 두 값을 이용해 util.IOUtil 클래스이 readDate() 로 body 를 추출해
* 나온 값으로 user 를 생성한다.
---------------------------------------
처음에 readDate() 를 사용해도 된다는걸 몰라서 생각보다 복잡했다. 
게다가 코드가 너무 길어져서 더이상 좋은 코드라고 보이지 않는다.
아직까지 객체지향적으로 생각하는법이 많이 부족하다고 느꼈다.

### 요구사항 4 - redirect 방식으로 이동
* 회원가입이 완료되면 /index.html 로 페이지를 이동시키고
* 응답 헤더에 status code 를 302 로 지정한다.
--------------------------------------
Response Header 에 Location : 주소 를 넣으면 url 를 바꿀수 있는걸
처음으로 알았다.
한군데에 모든 소스코드가 모이기 시작해 분산시켰다. 어찌저찌 요구사항은
하나씩 처내고 있긴 하지만 이제는 내가 만들고있는게 잘하고 있는건가
라는 의구심이 계속 들기 시작했다.

### 요구사항 5 - cookie
* 로그인이 성공하면 /index.html 로 실패하면 /user/login_failed.html
* 로 이동한다. 성공 여부에 따라 헤더에 Cookie 를 logined=true,false 로 넣어준다.
-------------------------------------
헤더를 넣어주는 코드를 정리 해야할때가 온것같다. 생각보다 헤더에 넣어야할 정보가
많고 나중에 http status code 의 종류가 늘어나면 감당이 어려울것 같다 느꼈다.

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 