<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Dev 목록" name="title" />
</jsp:include>
<table class="table w-75 mx-auto">
	<tr>
		<th scope="col">번호</th>
		<th scope="col">이름</th>
		<th scope="col">경력</th>
		<th scope="col">이메일</th>
		<th scope="col">성별</th>
		<th scope="col">개발가능언어</th>
	</tr>
	<c:if test="${not empty list}">
		<c:forEach items="${list}" var="dev" varStatus="vs">
			<tr>
				<td scope="row">${dev.no}</td>
				<td scope="row">${dev.name}</td>
				<td scope="row">${dev.career}</td>
				<td scope="row">${dev.email}</td>
				<td scope="row">${dev.gender}</td>
				<td scope="row"><c:forEach items="${dev.lang}" var="lang"
						varStatus="lvs">${lang}${lvs.last ? '':', '}
				</c:forEach></td>
				<td>
					<button class="btn btn-outline-secondary" onclick="updateDev(${dev.no});">수정</button>
					<button class="btn btn-outline-danger" onclick="deleteDev();"
						data-no="${dev.no}">삭제</button>
				</td>
					
			</tr>
	
		</c:forEach>
	</c:if>
</table>
					
		<form id="updateFrm">	
		<input type="hidden" name="no" value="">
		</form>
<script>
	function updateDev(no) {
		//get /demo/updateDev?no=123 --> devUpdateForm.jsp
		//post /demo/updateDev.do  --> redirect:/demo/devList.do
		var $frm = $("#updateFrm");
		$frm.find("[name=no]").val(no);
		$frm.attr("action",	"${pageContext.request.contextPath}/demo/updateDev?no="+no)
			.submit();

	};
	function deleteDev() {
		//post /demo/deleteDev.do --> redirect:/demo/devList.do
		/* var $devFrm = $("#devFrm");
		$devFrm.attr("action", `${pageContext.request.contextPath}/demo/\${id}.do`).attr("method", "post")
		.submit(); */
	}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
