flowableAdminApp.controller('ShowProcessInstanceDiagramPopupCtrl',
    ['$rootScope', '$scope', '$modalInstance', '$http', 'process', '$timeout', function ($rootScope, $scope, $modalInstance, $http, process, $timeout) {

        $scope.model = {
            id: process.id,
            name: process.name
        };

        $scope.status = {loading: false};

        $scope.cancel = function () {
            if (!$scope.status.loading) {
                $modalInstance.dismiss('cancel');
            }
        };

        $timeout(function () {
            $("#bpmnModel").attr("data-instance-id", process.id);
            $("#bpmnModel").attr("data-definition-id", process.processDefinitionId);
            $("#bpmnModel").attr("data-server-id", $rootScope.activeServers['process'].id);
            if (process.endTime != undefined) {
                $("#bpmnModel").attr("data-history-id", process.id);
            }
            // $("#bpmnModel").load("./display/displaymodel.html?instanceId=" + process.id);

        	
        	// var definitionId = $('#bpmnModel').attr('data-definition-id');
            // var instanceId = $('#bpmnModel').attr('data-instance-id');
            // var historyInstanceId = $('#bpmnModel').attr('data-history-id');
            // var serverId = $('#bpmnModel').attr('data-server-id');
            
            var definitionId = process.processDefinitionId;
            var instanceId = process.id;
            var serverId = $rootScope.activeServers['process'].id;
            if (process.endTime != undefined) {
                var historyInstanceId = process.id;
            }
         
        	function _showProcessDiagram() {
        	    var request;
	        	if (historyInstanceId) {
	        	   request = $.ajax({
                        type: 'get',
                        url: FlowableAdmin.Config.baseUrl + '/app/rest/admin/process-instances/' + historyInstanceId + '/history-model-json?processDefinitionId=' + definitionId + '&nocaching=' + new Date().getTime()
                    });
                    
	        	} else if (instanceId) {
					request = $.ajax({
					    type: 'get',
					    url: FlowableAdmin.Config.baseUrl + '/app/rest/admin/process-instances/' + instanceId + '/model-json?processDefinitionId=' + definitionId + '&nocaching=' + new Date().getTime()
					});
					
	        	} else {
					request = $.ajax({
					    type: 'get',
					    url: FlowableAdmin.Config.baseUrl + '/app/rest/admin/process-definitions/' + definitionId + '/model-json?nocaching=' + new Date().getTime()
					});
	        	}
	
				request.success(function(data, textStatus, jqXHR) {
					
					if (!data.elements || data.elements.length == 0) return;
					
					INITIAL_CANVAS_WIDTH = data.diagramWidth + 20;
					INITIAL_CANVAS_HEIGHT = data.diagramHeight + 50;
					canvasWidth = INITIAL_CANVAS_WIDTH;
					canvasHeight = INITIAL_CANVAS_HEIGHT;
					viewBoxWidth = INITIAL_CANVAS_WIDTH;
					viewBoxHeight = INITIAL_CANVAS_HEIGHT;
					
					var x = 0;
					if ($(window).width() > canvasWidth) {
						x = ($(window).width() - canvasWidth) / 2 - (data.diagramBeginX / 2);
					}
					
					var canvasValue = 'bpmnModel';
					
					$('#' + canvasValue).width(INITIAL_CANVAS_WIDTH);
					$('#' + canvasValue).height(INITIAL_CANVAS_HEIGHT);
					paper = Raphael(document.getElementById(canvasValue), canvasWidth, canvasHeight);
					paper.setViewBox(0, 0, viewBoxWidth, viewBoxHeight, false);
	          		paper.renderfix();
	          		
	          		if (data.pools) {
	          			for (var i = 0; i < data.pools.length; i++) {
					    	var pool = data.pools[i];
					    	_drawPool(pool, false, paper);
					    }
	          		}
	          		
				    var modelElements = data.elements;
				    for (var i = 0; i < modelElements.length; i++) {
				    	var element = modelElements[i];
				    	try {
				    		//console.log(element.type);
				    		var drawFunction = eval("_draw" + element.type);
				    		drawFunction(element, false, paper);
				    	} catch (err) {
							console.warn(err);
						}
				    }
				    
				    if (data.flows) {
					    for (var i = 0; i < data.flows.length; i++) {
					    	var flow = data.flows[i];
					    	_drawFlow(flow, paper);
					    }
				    }
				    
				    bpmnData = data;
				});
	
				request.error(function(jqXHR, textStatus, errorThrown) {
				    alert("error");
				});
				
				if (migrationDefinitionId && migrationDefinitionId.length > 0) {
                  migrationRequest = $.ajax({
                        type: 'get',
                        url: './app/rest/admin/process-definitions/' + migrationDefinitionId + '/model-json?nocaching=' + new Date().getTime()
                  });
                  
                  migrationRequest.success(function(data, textStatus, jqXHR) {
                    
                    if (!data.elements || data.elements.length == 0) return;
                    
                    INITIAL_CANVAS_WIDTH = data.diagramWidth + 20;
                    INITIAL_CANVAS_HEIGHT = data.diagramHeight + 50;
                    canvasWidth = INITIAL_CANVAS_WIDTH;
                    canvasHeight = INITIAL_CANVAS_HEIGHT;
                    viewBoxWidth = INITIAL_CANVAS_WIDTH;
                    viewBoxHeight = INITIAL_CANVAS_HEIGHT;
                    
                    var x = 0;
                    if ($(window).width() > canvasWidth) {
                        x = ($(window).width() - canvasWidth) / 2 - (data.diagramBeginX / 2);
                    }
                    
                    var canvasValue = 'targetModel';
                    
                    $('#' + canvasValue).width(INITIAL_CANVAS_WIDTH);
                    $('#' + canvasValue).height(INITIAL_CANVAS_HEIGHT);
                    migrationPaper = Raphael(document.getElementById(canvasValue), canvasWidth, canvasHeight);
                    migrationPaper.setViewBox(0, 0, viewBoxWidth, viewBoxHeight, false);
                    migrationPaper.renderfix();
                    
                    if (data.pools) {
                        for (var i = 0; i < data.pools.length; i++) {
                            var pool = data.pools[i];
                            _drawPool(pool, true, migrationPaper);
                        }
                    }
                    
                    var modelElements = data.elements;
                    for (var i = 0; i < modelElements.length; i++) {
                        var element = modelElements[i];
                        try {
                            var drawFunction = eval("_draw" + element.type);
                            drawFunction(element, true, migrationPaper);
                        } catch (err) {
                            console.warn(err);
                        }
                    }
                    
                    if (data.flows) {
                        for (var i = 0; i < data.flows.length; i++) {
                            var flow = data.flows[i];
                            _drawFlow(flow, migrationPaper);
                        }
                    }
                    
                    migrationData = data;
                  });
                }
			}
			
            _showProcessDiagram();



        }, 200);


    }]);