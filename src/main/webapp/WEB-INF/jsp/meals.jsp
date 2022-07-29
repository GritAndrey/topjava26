<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script src="resources/js/topjava.common.js" defer></script>
<script src="resources/js/topjava.meals.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>
        <div class="card border-dark">
            <div class="card-body pb-0">
                <form id="filterForm">
                    <div class="row">
                        <div class="col-2">
                            <label for="startDate"><spring:message code="meal.startDate"/>:</label>
                            <input class="form-control" type="date" name="startDate" id="startDate"
                                   value="${param.startDate}">
                        </div>
                        <div class="col-2">
                            <label for="endDate"><spring:message code="meal.endDate"/>:</label>
                            <input class="form-control" type="date" name="endDate" id="endDate"
                                   value="${param.endDate}">
                        </div>
                        <div class="offset-2 col-3">
                            <label for="startTime"><spring:message code="meal.startTime"/>:</label>
                            <input class="form-control" type="time" name="startTime" id="startTime"
                                   value="${param.startTime}">
                        </div>
                        <div class="col-3">
                            <label for="endTime"><spring:message code="meal.endTime"/>:</label>
                            <input class="form-control" type="time" name="endTime" id="endTime"
                                   value="${param.endTime}">
                        </div>
                    </div>
                </form>
                <div class="card-footer text-right">
                    <button class="btn btn-primary" onclick="addFilter()">
                        <span class="fa fa-filter"></span><spring:message code="meal.filter"/></button>
                    <button class="btn btn-danger" onclick="resetFilter()">
                        <span class="fa fa-remove"></span><spring:message code="common.cancel"/></button>
                </div>
            </div>
        </div>
        <br>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="meal.add"/>
        </button>
        <br>
        <div id="datatable_wrapper" class="dataTables_wrapper dt-bootstrap4 no-footer">
            <table class="table table-striped" id="datatable">
                <thead>
                <tr>
                    <th><spring:message code="meal.dateTime"/></th>
                    <th><spring:message code="meal.description"/></th>
                    <th><spring:message code="meal.calories"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.meals}" var="meal">
                    <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                    <tr data-meal-excess="${meal.excess}" id="${meal.id}">
                        <td>
                                ${fn:formatDateTime(meal.dateTime)}
                        </td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><a href="meals/update?id=${meal.id}"><spring:message code="common.update"/></a></td>
                        <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>

</section>
<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title"><spring:message code="${meal.isNew() ? 'meal.add' : 'meal.edit'}"/></h3>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" name="id" id="id">
                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/>:</label>
                        <input type="datetime-local" class="form-control"
                               placeholder="<spring:message code="meal.dateTime"/>" name="dateTime"
                               id="dateTime" required>
                    </div>
                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message
                                code="meal.description"/>:</label>
                        <input type="text" class="form-control" placeholder="<spring:message code="meal.description"/>"
                               size=40
                               name="description" id="description"
                               required>
                    </div>
                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/>:</label>
                        <input type="number" class="form-control" placeholder="<spring:message code="meal.calories"/>"
                               name="calories"
                               id="calories" required>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>