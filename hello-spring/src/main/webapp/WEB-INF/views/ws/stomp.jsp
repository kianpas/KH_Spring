<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Stomp" name="title"/>
</jsp:include>


	
<c:if test="${loginMember.id eq 'admin'}">
<div class="input-group mb-3">
	<select id="stomp-url" class="form-select mr-1">
		<option value="">전송url</option>
		<option value="/notice">/admin/notice</option>
		<option value="/notice/honggd">/admin/notice/honggd</option>
	</select>
    <input type="text" id="message" class="form-control" placeholder="Message">
    <div class="input-group-append" style="padding: 0px;">
        <button id="sendBtn" class="btn btn-outline-secondary" type="button">Send</button>
    </div>
   </div>
   <button id="ajaxBtn" class="btn btn-outline-primary" type="button">비동기요청</button>
 </c:if>
    <div>
        <ul class="list-group list-group-flush" id="data"></ul>
    </div>
   <!-- sockjs-client 추가 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.2/sockjs.min.js" integrity="sha512-2hPuJOZB0q6Eu4RlRRL2/8/MZ+IoSSxgDUu+eIUNzHOoHLUwf2xvrMFN4se9mu0qCgxIjHum6jdGk/uMiQoMpQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<!-- 구형 브라우저 지원을 위한 바벨 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/babel-standalone/6.26.0/babel.min.js" integrity="sha512-kp7YHLxuJDJcOzStgd6vtpxr4ZU9kjn77e6dBsivSz+pUuAuMlE2UTdKB7jjsWT84qbS8kdCWHPETnP/ctrFsA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/babel-polyfill/7.12.1/polyfill.min.js" integrity="sha512-uzOpZ74myvXTYZ+mXUsPhDF+/iL/n32GDxdryI2SJronkEyKC8FBFRLiBQ7l7U/PTYebDbgTtbqTa6/vGtU23A==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<!-- stomp 가져오기 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
$("#ajaxBtn").click(() => {
	$.ajax({
		url : "${pageContext.request.contextPath}/ws/someRequest.do",

		success(data){
			console.log(data)
		},
		error:console.log


	})
	
})
/**
 * sock.js
 * html5api websocket을 지원하지 않는 브라우저에서도 양방향 통신을 사용

 	http로 최초연결시도후, websock이 사용가능한 경우 ws protocol로 upgrade.
 	구번 브라우저의 경우, xhr-stream/xhr-polling 중 적합한 방식으로 양방향 통신 사용
 	localhost는 js var, 클라이언트에서 처리, $page는 el, 서버에서 처리
 */
const ws = new SockJS(`http://\${location.host}${pageContext.request.contextPath}/stomp`);
const stompClient = Stomp.over(ws);

const $data = $("#data");

//최초연결
stompClient.connect({}, frame => {
	console.log("stomp connected : ", frame);

	//구독
	stompClient.subscribe("/notice", frame => {
		console.log("message from /notice : ", frame);
		displayMessage(frame)
	});

	stompClient.subscribe("/notice/${loginMember.id}", frame => {
		console.log("message from /notice/${loginMember.id} : ", frame);
		displayMessage(frame)
	});
	
});

const displayMessage = ({body}) => {
	//1. json -> js object
	let obj = JSON.parse(body);
	console.log(obj);

	//2. 내용만 구조분해할당
	const {message} = obj;
	let html = 	`<div class="alert alert-warning alert-dismissible fade show" role="alert">
	  <strong>\${message}</strong>
	  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
	    <span aria-hidden="true">&times;</span>
	  </button>
	</div>`;

	//3. #content prepend(자식요소로 맨앞에 추가하기)
	const $container = $("#content");
	$container.prepend(html);
}


const sendMessage = () =>{
	const url = $("#stomp-url").val();
	console.log(url);
	if(url === ""){
		alert("전송 url을 선택하세요.");
		return;
	}
	
	const $message = $("#message");

	const msg = {from : "${loginMember.id}",
				 to : url === "/notice" ? "all" : "honggd",
				 message : $message.val(),
				 type : "NOTICE", 
				 time : Date.now()
			}
	
	if($message.val()){
		stompClient.send(url, {}, JSON.stringify(msg));
		$message.val("").focus();
	}
}
$("#sendBtn").click(sendMessage);
$("#message").keyup(e=> e.keyCode == 13 && sendMessage());
</script>    

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>