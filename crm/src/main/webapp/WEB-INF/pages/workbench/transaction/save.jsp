<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path=request.getContextPath();
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
</head>
<script type="text/javascript">
	$(function () {
		$("#saveTranBtn").click(function () {
			var name=$("#create-transactionName").val();
			var owner=$("#create-Owner").val();
			var money=$("#create-amountOfMoney").val();
			var expectedDate=$("#create-expectedClosingDate").val();
			var customerId=$("#customerId").val();
			var stage=$("#create-transactionStage").val();
			var type=$("#create-transactionType").val();
			var source=$("#create-source").val();
			var activityId=$("#activityId").val();
			var contactsId=$("#contactsId").val();
			var description=$("#create-description").val();
			var contactSummary=$("#create-contactSummary").val();
			var nextContactTime=$("#create-nextContactTime").val();
			$.ajax({
				url:"workbench/transaction/saveTransAction.do",
				data:{
					owner:owner,
					money:money,
					name:name,
					expectedDate:expectedDate,
					customerId:customerId,
					stage:stage,
					type:type,
					source:source,
					activityId:activityId,
					contactsId:customerId,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
				},
				dataType:"json",
				type:"post",
				success:function (data) {
					if(data.code==1){
						window.location.href="workbench/transaction/index.do"
					}else{
						alert("????????????");
					}
				}
			})
		})

		$("#A_body").on("click", "input[type='radio']",function () {
			var name=$("#A_body input[type='radio']").attr("remarkName");
			$("#create-activitySrc").val(name);
			$("#activityId").val(this.value);
			$("#findMarketActivity").modal("hide");
		})
		$("#C_body").on("click", "input[type='radio']",function () {
			var name=$("#C_body input[type='radio']").attr("remarkName");
			$("#create-contactsName").val(name);
			$("#contactsId").val(this.value);
			$("#findContacts").modal("hide");
		})
	$("#i_activityName").keyup(function () {
		var activityName=this.value;
		$.ajax({
			url:"workbench/transaction/searchActivityList.do",
			type:"get",
			data:{"activityName":activityName},
			dataType: "json",
			success:function (data) {
				var html="";
				$.each(data,function (i,obj) {
				html+="<tr>"
				html+="<td><input remarkName='"+obj.name+"'type=\"radio\" name=\"activity\" value='"+obj.id+"'/></td>"
				html+="<td>"+obj.name+"</td>"
				html+="<td>"+obj.startDate+"</td>";
				html+="<td>"+obj.endDate+"</td>"
				html+="<td>"+obj.owner+"</td>"
				html+="</tr>"
				})
				$("#A_body").html(html);
			}
		})
	})
		$("#i_contactsName").keyup(function () {
			var contactsName=this.value;
			$.ajax({
				url:"workbench/transaction/searchContactsList.do",
				type:"get",
				data:{"contactsName":contactsName},
				dataType: "json",
				success:function (data) {
					var html="";
					$.each(data,function (i,obj) {
						html+="<tr>"
						html+="<td><input remarkName='"+obj.fullname+"' type=\"radio\" name=\"activity\"value='\"+obj.id+\"'/></td>"
						html+="<td>"+obj.fullname+"</td>"
						html+="<td>"+obj.email+"</td>";
						html+="<td>"+obj.mphone+"</td>"
						html+="</tr>"
					})
					$("#C_body").html(html);
				}
			})
		})
		$("#create-transactionStage").click(function () {
			var stageValue=this.value;
			$.ajax({
				url:"workbench/transaction/getPossibilityByStage.do",
				data:{
					stageValue:stageValue
				},
				dataType:"json",
				type:"get",
				success:function (data) {
					$("#create-possibility").val(data+"%");
				}
			})
		})
	$("#create-accountName").typeahead({
		source: function (query, process) {
			return $.ajax({
			url: 'workbench/transaction/searchCustomerList.do',
			type: 'post',
			data: {customerName: query},
			dataType: 'json',
			success: function (result) {

					return process(result);
			}
		});
	},
	matcher: function (obj) {

		return ~obj.name.toLowerCase().indexOf(this.query.toLowerCase())
	},

	updater: function (obj) {
		$('#customerId').attr('value', obj.id);
		return obj.name;
	}

	})
	})

</script>
<body>

	<!-- ?????????????????? -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">??</span>
					</button>
					<h4 class="modal-title">??????????????????</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input id="i_activityName" type="text" class="form-control" style="width: 300px;" placeholder="????????????????????????????????????????????????">

							  <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>??????</td>
								<td>????????????</td>
								<td>????????????</td>
								<td>?????????</td>
							</tr>
						</thead>
						<tbody id="A_body">

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- ??????????????? -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">??</span>
					</button>
					<h4 class="modal-title">???????????????</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input id="i_contactsName" type="text" class="form-control" style="width: 300px;" placeholder="?????????????????????????????????????????????">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>??????</td>
								<td>??????</td>
								<td>??????</td>
							</tr>
						</thead>
						<tbody id="C_body">

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>????????????</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveTranBtn" type="button" class="btn btn-primary">??????</button>
			<button type="button" class="btn btn-default">??????</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner">
				  <c:forEach items="${userList}" var="user">
				  	<option value="${user.id}">${user.name}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">??????</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">??????????????????<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-expectedClosingDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">????????????<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input data-provide="typeahead" autocomplete="off" type="text" class="form-control" id="create-accountName" placeholder="???????????????????????????????????????????????????">
				<input id="customerId" type="hidden">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage">
				  <c:forEach items="${stageList}" var="stage">
					  <option value="${stage.id}">${stage.value}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">??????</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType">
				  <option></option>
				<c:forEach items="${typeList}" var="type">
					<option value="${type.id}">${type.value}</option>
				</c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">?????????</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">??????</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource">
				  <option></option>
					<c:forEach items="${sourceList}" var="source">
						<option value="${source.id}">${source.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">???????????????&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-activitySrc">
				<input type="hidden" id="activityId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">???????????????&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName">
				<input type="hidden" id="contactsId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">??????</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">????????????</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">??????????????????</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>