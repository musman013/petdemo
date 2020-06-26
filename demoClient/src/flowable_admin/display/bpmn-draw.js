/* Copyright 2005-2015 Alfresco Software, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var NORMAL_STROKE = 2;
var SEQUENCEFLOW_STROKE = 2;
var TASK_STROKE = 2;
var CALL_ACTIVITY_STROKE = 4;
var ENDEVENT_STROKE = 4;
var CURRENT_ACTIVITY_STROKE = 4;

var COMPLETED_COLOR = "#2674aa";
var TEXT_COLOR = "#373e48";
var CURRENT_COLOR = "#66AA66";
var HOVER_COLOR = "#d5bc01";
var ACTIVITY_STROKE_COLOR = "#000000";
var ACTIVITY_FILL_COLOR = "#ffffff";
var MAIN_STROKE_COLOR = "#585858";

var TEXT_PADDING = 3;
var ARROW_WIDTH = 6;
var MARKER_WIDTH = 12;

var TASK_FONT = { font: "11px Arial", opacity: 1, fill: Raphael.rgb(0, 0, 0) };

// icons
var ICON_SIZE = 16;
var ICON_PADDING = 4;

var INITIAL_CANVAS_WIDTH;
var INITIAL_CANVAS_HEIGHT;

var paper;
var migrationPaper;
var viewBox;
var viewBoxWidth;
var viewBoxHeight;

var canvasWidth;
var canvasHeight;

var elementsAdded = new Array();
var elementsRemoved = new Array();
var changeStateStartElementIds = new Array();
var changeStateStartElements = new Array();
var changeStateStartGlowElements = new Array();
var changeStateEndElementIds = new Array();
var changeStateEndElements = new Array();
var changeStateEndGlowElements = new Array();

var migrationMappedElements = new Array();
var migrationMappedElementsText = new Array();

var collapsedItemNavigation = new Array();
var bpmnData;
var migrationData;

var migrationDefinitionId = $('#targetModel').attr('data-migration-definition-id');

function _showTip(htmlNode, element) {
	if (migrationDefinitionId && migrationDefinitionId.length > 0) return;
	var documentation = "";
	if (element.name && element.name.length > 0) {
		documentation += "<b>Name</b>: <i>" + element.name + "</i><br/><br/>";
	}

	var text;
	if (element.name && element.name.length > 0) {
		text = element.name;
	} else {
		text = element.id;
	}

	htmlNode.qtip({
		content: {
			text: documentation,
			title: {
				text: text
			}
		},
		position: {
			my: 'top left',
			at: 'bottom center',
			viewport: $(window)
		},
		hide: {
			fixed: true, delay: 100,
			event: 'click mouseleave'
		},
		style: {
			classes: 'ui-tooltip-kisbpm-bpmn'
		}
	});
}

function _expandCollapsedElement(element) {
	if (bpmnData.collapsed) {
		for (var i = 0; i < bpmnData.collapsed.length; i++) {
			var collapsedItem = bpmnData.collapsed[i];
			if (element.id == collapsedItem.id) {
				paper.clear();

				var modelElements = collapsedItem.elements;
				for (var i = 0; i < modelElements.length; i++) {
					var subElement = modelElements[i];
					var drawFunction = eval("_draw" + subElement.type);
					drawFunction(subElement);
				}

				if (collapsedItem.flows) {
					for (var i = 0; i < collapsedItem.flows.length; i++) {
						var subFlow = collapsedItem.flows[i];
						_drawFlow(subFlow);
					}
				}

				var collapsedName;
				if (element.name) {
					collapsedName = element.name;
				} else {
					collapsedName = 'sub process ' + element.id;
				}

				collapsedItemNavigation.push({
					"id": element.id,
					"name": collapsedName
				});

				_buildNavigationTree();

				break;
			}
		}
	}
}

function _navigateTo(elementId) {
	var modelElements = undefined;
	var modelFlows = undefined;
	newCollapsedItemNavigation = new Array();

	if (elementId == 'FLOWABLE_ROOT_PROCESS') {
		modelElements = bpmnData.elements;
		modelFlows = bpmnData.flows;

	} else {

		for (var i = 0; i < bpmnData.collapsed.length; i++) {
			var collapsedItem = bpmnData.collapsed[i];

			var collapsedName = undefined;
			for (var j = 0; j < collapsedItemNavigation.length; j++) {
				if (elementId == collapsedItemNavigation[j].id) {
					collapsedName = collapsedItemNavigation[j].name;
					break;
				}
			}

			if (!collapsedName) {
				continue;
			}

			newCollapsedItemNavigation.push({
				"id": collapsedItem.id,
				"name": collapsedName
			});

			if (elementId == collapsedItem.id) {
				modelElements = collapsedItem.elements;
				modelFlows = collapsedItem.flows;
				break;
			}
		}
	}

	if (modelElements) {
		paper.clear();

		for (var i = 0; i < modelElements.length; i++) {
			var subElement = modelElements[i];
			var drawFunction = eval("_draw" + subElement.type);
			drawFunction(subElement);
		}

		if (modelFlows) {
			for (var i = 0; i < modelFlows.length; i++) {
				var subFlow = modelFlows[i];
				_drawFlow(subFlow);
			}
		}

		collapsedItemNavigation = newCollapsedItemNavigation;

		_buildNavigationTree();
	}
}

function _buildNavigationTree() {
	var navigationUrl = '| <a href="javascript:_navigateTo(\'FLOWABLE_ROOT_PROCESS\')">Root</a>';

	for (var i = 0; i < collapsedItemNavigation.length; i++) {
		navigationUrl += ' > <a href="javascript:_navigateTo(\'' + collapsedItemNavigation[i].id + '\')">' +
			collapsedItemNavigation[i].name + '</a>';
	}

	$('#navigationTree').html(navigationUrl);
}

function _addHoverLogic(element, type, defaultColor, isMigrationModelElement, currentPaper) {
	var strokeColor = _bpmnGetColor(element, defaultColor);
	var topBodyRect;
	if (type === "rect") {
		topBodyRect = currentPaper.rect(element.x, element.y, element.width, element.height);

	} else if (type === "circle") {
		var x = element.x + (element.width / 2);
		var y = element.y + (element.height / 2);
		topBodyRect = currentPaper.circle(x, y, 15);

	} else if (type === "rhombus") {
		topBodyRect = currentPaper.path("M" + element.x + " " + (element.y + (element.height / 2)) +
			"L" + (element.x + (element.width / 2)) + " " + (element.y + element.height) +
			"L" + (element.x + element.width) + " " + (element.y + (element.height / 2)) +
			"L" + (element.x + (element.width / 2)) + " " + element.y + "z"
		);
	}

	if (isMigrationModelElement) {

		topBodyRect.attr({
			"opacity": 0,
			"stroke": "none",
			"fill": "#ffffff"
		});

		topBodyRect.click(function () {
			var endElementIndex = $.inArray(element.id, changeStateEndElementIds);
			if (endElementIndex >= 0) {

				var glowElement = changeStateEndGlowElements[endElementIndex];
				glowElement.remove();

				changeStateEndGlowElements.splice(endElementIndex, 1);
				changeStateEndElementIds.splice(endElementIndex, 1);
				changeStateEndElements.splice(endElementIndex, 1);

			} else {
				var endGlowElement = topBodyRect.glow({ 'color': 'red' });
				changeStateEndGlowElements.push(endGlowElement);
				changeStateEndElementIds.push(element.id);
				changeStateEndElements.push(element);
			}

			if (changeStateStartElements.length > 0 && changeStateEndElements.length > 0) {
				$('#addMappingButton').show();

			} else {
				$('#addMappingButton').hide();
			}
		});

	} else {
		var opacity = 0;
		var fillColor = "#ffffff";
		if ($.inArray(element.id, elementsAdded) >= 0) {
			opacity = 0.2;
			fillColor = "green";
		}

		if ($.inArray(element.id, elementsRemoved) >= 0) {
			opacity = 0.2;
			fillColor = "red";
		}

		topBodyRect.attr({
			"opacity": opacity,
			"stroke": "none",
			"fill": fillColor
		});
		_showTip($(topBodyRect.node), element);

		topBodyRect.click(function () {
			if (migrationDefinitionId && migrationDefinitionId.length > 0) {
				var startElementIndex = $.inArray(element.id, changeStateStartElementIds);
				if (startElementIndex >= 0) {

					var glowElement = changeStateStartGlowElements[startElementIndex];
					glowElement.remove();

					changeStateStartGlowElements.splice(startElementIndex, 1);
					changeStateStartElementIds.splice(startElementIndex, 1);
					changeStateStartElements.splice(startElementIndex, 1);

				} else {
					var startGlowElement = topBodyRect.glow({ 'color': 'blue' });
					changeStateStartGlowElements.push(startGlowElement);
					changeStateStartElementIds.push(element.id);
					changeStateStartElements.push(element);
				}

				if (changeStateStartElements.length > 0 && changeStateEndElements.length > 0) {
					$('#addMappingButton').show();

				} else {
					$('#addMappingButton').hide();
				}

			} else {
				var startElementIndex = $.inArray(element.id, changeStateStartElementIds);
				var endElementIndex = $.inArray(element.id, changeStateEndElementIds);
				if (startElementIndex >= 0) {

					var glowElement = changeStateStartGlowElements[startElementIndex];
					glowElement.remove();

					changeStateStartGlowElements.splice(startElementIndex, 1);
					changeStateStartElementIds.splice(startElementIndex, 1);
					changeStateStartElements.splice(startElementIndex, 1);

				} else if (endElementIndex >= 0) {

					var glowElement = changeStateEndGlowElements[endElementIndex];
					glowElement.remove();

					changeStateEndGlowElements.splice(endElementIndex, 1);
					changeStateEndElementIds.splice(endElementIndex, 1);
					changeStateEndElements.splice(endElementIndex, 1);

				} else {
					if (element.current) {
						var startGlowElement = topBodyRect.glow({ 'color': 'blue' });
						changeStateStartGlowElements.push(startGlowElement);
						changeStateStartElementIds.push(element.id);
						changeStateStartElements.push(element);

					} else {
						var endGlowElement = topBodyRect.glow({ 'color': 'red' });
						changeStateEndGlowElements.push(endGlowElement);
						changeStateEndElementIds.push(element.id);
						changeStateEndElements.push(element);
					}
				}

				if (changeStateStartElements.length > 0 && changeStateEndElements.length > 0) {
					$('#changeStateButton').show();
				} else {
					$('#changeStateButton').hide();
				}
			}
		});
	}

	topBodyRect.mouseover(function () {
		currentPaper.getById(element.id).attr({ "stroke": HOVER_COLOR });
	});

	topBodyRect.mouseout(function () {
		currentPaper.getById(element.id).attr({ "stroke": strokeColor });
	});
}

function _zoom(zoomIn) {
	var tmpCanvasWidth, tmpCanvasHeight;
	if (zoomIn) {
		tmpCanvasWidth = canvasWidth * (1.0 / 0.90);
		tmpCanvasHeight = canvasHeight * (1.0 / 0.90);
	} else {
		tmpCanvasWidth = canvasWidth * (1.0 / 1.10);
		tmpCanvasHeight = canvasHeight * (1.0 / 1.10);
	}

	if (tmpCanvasWidth != canvasWidth || tmpCanvasHeight != canvasHeight) {
		canvasWidth = tmpCanvasWidth;
		canvasHeight = tmpCanvasHeight;
		paper.setSize(canvasWidth, canvasHeight);
	}
}

function _bpmnGetColor(element, defaultColor) {
	var strokeColor;
	if (element.current) {
		strokeColor = CURRENT_COLOR;
	} else if (element.completed) {
		strokeColor = COMPLETED_COLOR;
	} else {
		strokeColor = defaultColor;
	}
	return strokeColor;
}

function _drawPool(pool, isMigrationModelElement, currentPaper) {
	var rect = currentPaper.rect(pool.x, pool.y, pool.width, pool.height);

	rect.attr({
		"stroke-width": 1,
		"stroke": MAIN_STROKE_COLOR,
		"fill": "white"
	});

	if (pool.name) {
		var poolName = currentPaper.text(pool.x + 14, pool.y + (pool.height / 2), pool.name).attr({
			"text-anchor": "middle",
			"font-family": "Arial",
			"font-size": "12",
			"fill": MAIN_STROKE_COLOR
		});

		poolName.transform("r270");
	}

	if (pool.lanes) {
		for (var i = 0; i < pool.lanes.length; i++) {
			var lane = pool.lanes[i];
			_drawLane(lane, isMigrationModelElement, currentPaper);
		}
	}
}

function _drawLane(lane, isMigrationModelElement, currentPaper) {
	var rect = currentPaper.rect(lane.x, lane.y, lane.width, lane.height);

	rect.attr({
		"stroke-width": 1,
		"stroke": MAIN_STROKE_COLOR,
		"fill": "white"
	});

	if (lane.name) {
		var laneName = currentPaper.text(lane.x + 10, lane.y + (lane.height / 2), lane.name).attr({
			"text-anchor": "middle",
			"font-family": "Arial",
			"font-size": "12",
			"fill": MAIN_STROKE_COLOR
		});

		laneName.transform("r270");
	}
}

function _drawSubProcess(element, isMigrationModelElement, currentPaper) {
	var rect = currentPaper.rect(element.x, element.y, element.width, element.height, 4);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	rect.attr({
		"stroke-width": 1,
		"stroke": strokeColor,
		"fill": "white"
	});

	if (element.collapsed) {
		if (element.name) {
			this._drawMultilineText(element.name, element.x, element.y, element.width, element.height, "middle", "middle", 11,
				_bpmnGetColor(element, TEXT_COLOR), currentPaper);
		}

		rect.click(function () {
			_expandCollapsedElement(element);
		});
	}
}

function _drawEventSubProcess(element, isMigrationModelElement, currentPaper) {
	var rect = currentPaper.rect(element.x, element.y, element.width, element.height, 4);
	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	rect.attr({
		"stroke-width": 2,
		"stroke": strokeColor,
		"stroke-dasharray": ".",
		"fill": "white"
	});
}

function _drawStartEvent(element, isMigrationModelElement, currentPaper) {
	var startEvent = _drawEvent(element, NORMAL_STROKE, 15, currentPaper);
	startEvent.click(function () {
		_zoom(true);
	});
	_addHoverLogic(element, "circle", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawEndEvent(element, isMigrationModelElement, currentPaper) {
	var endEvent = _drawEvent(element, ENDEVENT_STROKE, 14, currentPaper);
	endEvent.click(function () {
		_zoom(false);
	});
	_addHoverLogic(element, "circle", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawEvent(element, strokeWidth, radius, currentPaper) {
	var x = element.x + (element.width / 2);
	var y = element.y + (element.height / 2);

	var circle = currentPaper.circle(x, y, radius);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);
	circle.attr({
		"stroke-width": strokeWidth,
		"stroke": strokeColor,
		"fill": "#ffffff"
	});

	circle.id = element.id;

	_drawEventIcon(currentPaper, element);

	return circle;
}

function _drawServiceTask(element, isMigrationModelElement, currentPaper) {
	_drawTask(element, currentPaper);
	if (element.taskType === "mail") {
		_drawSendTaskIcon(currentPaper, element.x - 4, element.y - 4, element);

	} else if (element.taskType === "camel") {
		_drawCamelTaskIcon(currentPaper, element.x + 4, element.y + 4);

	} else if (element.taskType === "mule") {
		_drawMuleTaskIcon(currentPaper, element.x + 4, element.y + 4);

	} else if (element.taskType === "http") {
		_drawHttpTaskIcon(currentPaper, element.x + 4, element.y + 4);

	} else if (element.taskType === "dmn") {
		_drawDecisionTaskIcon(currentPaper, element.x + 4, element.y + 4);

	} else if (element.taskType === "shell") {
		_drawShellTaskIcon(currentPaper, element.x + 4, element.y + 4);

	} else if (element.stencilIconId) {
		currentPaper.image("../service/stencilitem/" + element.stencilIconId + "/icon", element.x + 4, element.y + 4, 16, 16);

	} else {
		_drawServiceTaskIcon(currentPaper, element.x + 4, element.y + 4, element);
	}
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawHttpServiceTask(element, isMigrationModelElement, currentPaper) {
	_drawTask(element, currentPaper);
	_drawHttpTaskIcon(currentPaper, element.x + 4, element.y + 4);
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawCallActivity(element, isMigrationModelElement, currentPaper) {
	var width = element.width - (CALL_ACTIVITY_STROKE / 2);
	var height = element.height - (CALL_ACTIVITY_STROKE / 2);

	var rect = currentPaper.rect(element.x, element.y, width, height, 4);


	var strokeColor = _bpmnGetColor(element, ACTIVITY_STROKE_COLOR);

	rect.attr({
		"stroke-width": CALL_ACTIVITY_STROKE,
		"stroke": strokeColor,
		"fill": ACTIVITY_FILL_COLOR
	});

	rect.id = element.id;

	if (element.name) {
		this._drawMultilineText(element.name, element.x, element.y, element.width, element.height, "middle", "middle", 11, null, currentPaper);
	}
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawScriptTask(element, isMigrationModelElement, currentPaper) {
	_drawTask(element, currentPaper);
	_drawScriptTaskIcon(currentPaper, element.x + 4, element.y + 4, element);
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawUserTask(element, isMigrationModelElement, currentPaper) {
	_drawTask(element, currentPaper);
	_drawUserTaskIcon(currentPaper, element.x + 4, element.y + 4, element);
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawBusinessRuleTask(element, isMigrationModelElement, currentPaper) {
	_drawTask(element, currentPaper);
	_drawBusinessRuleTaskIcon(currentPaper, element.x + 4, element.y + 4, element);
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawManualTask(element, isMigrationModelElement, currentPaper) {
	_drawTask(element, currentPaper);
	_drawManualTaskIcon(currentPaper, element.x + 4, element.y + 4, element);
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawReceiveTask(element, isMigrationModelElement, currentPaper) {
	_drawTask(element, currentPaper);
	_drawReceiveTaskIcon(currentPaper, element.x, element.y, element);
	_addHoverLogic(element, "rect", ACTIVITY_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawTask(element, currentPaper) {
	var width = element.width - (TASK_STROKE / 2);
	var height = element.height - (TASK_STROKE / 2);

	var rect = currentPaper.rect(element.x, element.y, width, height, 4);


	var strokeColor = _bpmnGetColor(element, ACTIVITY_STROKE_COLOR);
	var strokeWidth = element.current ? CURRENT_ACTIVITY_STROKE : TASK_STROKE;
	rect.attr({
		"stroke-width": strokeWidth,
		"stroke": strokeColor,
		"fill": ACTIVITY_FILL_COLOR
	});

	rect.id = element.id;

	if (element.name) {
		this._drawMultilineText(element.name, element.x, element.y, element.width, element.height, "middle", "middle", 11,
			_bpmnGetColor(element, TEXT_COLOR), currentPaper);
	}
}

function _drawExclusiveGateway(element, isMigrationModelElement, currentPaper) {
	_drawGateway(element, currentPaper);
	var quarterWidth = element.width / 4;
	var quarterHeight = element.height / 4;

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	var iks = currentPaper.path(
		"M" + (element.x + quarterWidth + 3) + " " + (element.y + quarterHeight + 3) +
		"L" + (element.x + 3 * quarterWidth - 3) + " " + (element.y + 3 * quarterHeight - 3) +
		"M" + (element.x + quarterWidth + 3) + " " + (element.y + 3 * quarterHeight - 3) +
		"L" + (element.x + 3 * quarterWidth - 3) + " " + (element.y + quarterHeight + 3)
	);
	iks.attr({ "stroke-width": 3, "stroke": strokeColor });

	_addHoverLogic(element, "rhombus", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawParallelGateway(element, isMigrationModelElement, currentPaper) {
	_drawGateway(element, currentPaper);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	var path1 = currentPaper.path("M 6.75,16 L 25.75,16 M 16,6.75 L 16,25.75");
	path1.attr({
		"stroke-width": 3,
		"stroke": strokeColor,
		"fill": "none"
	});

	path1.transform("T" + (element.x + 4) + "," + (element.y + 4));

	_addHoverLogic(element, "rhombus", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawInclusiveGateway(element, isMigrationModelElement, currentPaper) {
	_drawGateway(element, currentPaper);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	var circle1 = currentPaper.circle(element.x + (element.width / 2), element.y + (element.height / 2), 9.75);
	circle1.attr({
		"stroke-width": 2.5,
		"stroke": strokeColor,
		"fill": "none"
	});

	_addHoverLogic(element, "rhombus", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawEventGateway(element, isMigrationModelElement, currentPaper) {
	_drawGateway(element, currentPaper);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	var circle1 = currentPaper.circle(element.x + (element.width / 2), element.y + (element.height / 2), 10.4);
	circle1.attr({
		"stroke-width": 0.5,
		"stroke": strokeColor,
		"fill": "none"
	});

	var circle2 = currentPaper.circle(element.x + (element.width / 2), element.y + (element.height / 2), 11.7);
	circle2.attr({
		"stroke-width": 0.5,
		"stroke": strokeColor,
		"fill": "none"
	});

	var path1 = currentPaper.path("M 20.327514,22.344972 L 11.259248,22.344216 L 8.4577203,13.719549 L 15.794545,8.389969 L 23.130481,13.720774 L 20.327514,22.344972 z");
	path1.attr({
		"stroke-width": 1.39999998,
		"stroke": strokeColor,
		"fill": "none",
		"stroke-linejoin": "bevel"
	});

	path1.transform("T" + (element.x + 4) + "," + (element.y + 4));

	_addHoverLogic(element, "rhombus", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);
}

function _drawGateway(element, currentPaper) {
	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	var rhombus = currentPaper.path("M" + element.x + " " + (element.y + (element.height / 2)) +
		"L" + (element.x + (element.width / 2)) + " " + (element.y + element.height) +
		"L" + (element.x + element.width) + " " + (element.y + (element.height / 2)) +
		"L" + (element.x + (element.width / 2)) + " " + element.y + "z"
	);

	rhombus.attr("stroke-width", 2);
	rhombus.attr("stroke", strokeColor);
	rhombus.attr({ fill: "#ffffff" });

	rhombus.id = element.id;

	return rhombus;
}

function _drawBoundaryEvent(element, isMigrationModelElement, currentPaper) {
	var x = element.x + (element.width / 2);
	var y = element.y + (element.height / 2);

	var circle = currentPaper.circle(x, y, 15);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	circle.attr({
		"stroke-width": 1,
		"stroke": strokeColor,
		"fill": "white"
	});

	var innerCircle = currentPaper.circle(x, y, 12);

	innerCircle.attr({
		"stroke-width": 1,
		"stroke": strokeColor,
		"fill": "none"
	});

	_drawEventIcon(currentPaper, element);
	_addHoverLogic(element, "circle", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);

	circle.id = element.id;
	innerCircle.id = element.id + "_inner";
}

function _drawIntermediateCatchEvent(element, isMigrationModelElement, currentPaper) {
	var x = element.x + (element.width / 2);
	var y = element.y + (element.height / 2);

	var circle = currentPaper.circle(x, y, 15);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	circle.attr({
		"stroke-width": 1,
		"stroke": strokeColor,
		"fill": "white"
	});

	var innerCircle = currentPaper.circle(x, y, 12);

	innerCircle.attr({
		"stroke-width": 1,
		"stroke": strokeColor,
		"fill": "none"
	});

	_drawEventIcon(currentPaper, element);
	_addHoverLogic(element, "circle", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);

	circle.id = element.id;
	innerCircle.id = element.id + "_inner";
}

function _drawThrowEvent(element, isMigrationModelElement, currentPaper) {
	var x = element.x + (element.width / 2);
	var y = element.y + (element.height / 2);

	var circle = currentPaper.circle(x, y, 15);

	var strokeColor = _bpmnGetColor(element, MAIN_STROKE_COLOR);

	circle.attr({
		"stroke-width": 1,
		"stroke": strokeColor,
		"fill": "white"
	});

	var innerCircle = currentPaper.circle(x, y, 12);

	innerCircle.attr({
		"stroke-width": 1,
		"stroke": strokeColor,
		"fill": "none"
	});

	_drawEventIcon(currentPaper, element);
	_addHoverLogic(element, "circle", MAIN_STROKE_COLOR, isMigrationModelElement, currentPaper);

	circle.id = element.id;
	innerCircle.id = element.id + "_inner";
}

function _drawMultilineText(text, x, y, boxWidth, boxHeight, horizontalAnchor, verticalAnchor, fontSize, color, currentPaper) {
	if (!text || text == "") {
		return;
	}

	var textBoxX = 0, textBoxY;
	var width = boxWidth - (2 * TEXT_PADDING);

	if (horizontalAnchor === "middle") {
		textBoxX = x + (boxWidth / 2);
	}
	else if (horizontalAnchor === "start") {
		textBoxX = x;
	}

	textBoxY = y + (boxHeight / 2);

	if (!color) {
		color = TEXT_COLOR;
	}
	var t = currentPaper.text(textBoxX + TEXT_PADDING, textBoxY + TEXT_PADDING).attr({
		"text-anchor": horizontalAnchor,
		"font-family": "Arial",
		"font-size": fontSize,
		"fill": color
	});

	var abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	t.attr({
		"text": abc
	});
	var letterWidth = t.getBBox().width / abc.length;

	t.attr({
		"text": text
	});
	var removedLineBreaks = text.split("\n");
	var x = 0, s = [];
	for (var r = 0; r < removedLineBreaks.length; r++) {
		var words = removedLineBreaks[r].split(" ");
		for (var i = 0; i < words.length; i++) {

			var l = words[i].length;
			if (x + (l * letterWidth) > width) {
				s.push("\n");
				x = 0;
			}
			x += l * letterWidth;
			s.push(words[i] + " ");
		}
		s.push("\n");
		x = 0;
	}
	t.attr({
		"text": s.join("")
	});

	if (verticalAnchor && verticalAnchor === "top") {
		t.attr({ "y": y + (t.getBBox().height / 2) });
	}
}

function _drawFlow(flow, currentPaper) {

	var polyline = new Polyline(flow.id, flow.waypoints, SEQUENCEFLOW_STROKE, currentPaper);

	var strokeColor = _bpmnGetColor(flow, MAIN_STROKE_COLOR);

	polyline.element = currentPaper.path(polyline.path);
	polyline.element.attr({ "stroke-width": SEQUENCEFLOW_STROKE });
	polyline.element.attr({ "stroke": strokeColor });

	polyline.element.id = flow.id;

	var lastLineIndex = polyline.getLinesCount() - 1;
	var line = polyline.getLine(lastLineIndex);

	if (flow.type == "connection" && flow.conditions) {
		var middleX = (line.x1 + line.x2) / 2;
		var middleY = (line.y1 + line.y2) / 2;
		var image = currentPaper.image("../editor/images/condition-flow.png", middleX - 8, middleY - 8, 16, 16);
	}

	var polylineInvisible = new Polyline(flow.id, flow.waypoints, SEQUENCEFLOW_STROKE, currentPaper);

	polylineInvisible.element = currentPaper.path(polyline.path);
	polylineInvisible.element.attr({
		"opacity": 0,
		"stroke-width": 8,
		"stroke": "#000000"
	});

	if (flow.name) {
		var firstLine = polyline.getLine(0);

		var angle;
		if (firstLine.x1 !== firstLine.x2) {
			angle = Math.atan((firstLine.y2 - firstLine.y1) / (firstLine.x2 - firstLine.x1));
		} else if (firstLine.y1 < firstLine.y2) {
			angle = Math.PI / 2;
		} else {
			angle = -Math.PI / 2;
		}
		var flowName = currentPaper.text(firstLine.x1, firstLine.y1, flow.name).attr({
			"text-anchor": "middle",
			"font-family": "Arial",
			"font-size": "12",
			"fill": "#000000"
		});

		var offsetX = (flowName.getBBox().width / 2 + 5);
		var offsetY = -(flowName.getBBox().height / 2 + 5);

		if (firstLine.x1 > firstLine.x2) {
			offsetX = -offsetX;
		}
		var rotatedOffsetX = offsetX * Math.cos(angle) - offsetY * Math.sin(angle);
		var rotatedOffsetY = offsetX * Math.sin(angle) + offsetY * Math.cos(angle);

		flowName.attr({
			x: firstLine.x1 + rotatedOffsetX,
			y: firstLine.y1 + rotatedOffsetY
		});

		flowName.transform("r" + ((angle) * 180) / Math.PI);
	}

	_showTip($(polylineInvisible.element.node), flow);

	polylineInvisible.element.mouseover(function () {
		currentPaper.getById(polyline.element.id).attr({ "stroke": HOVER_COLOR });
	});

	polylineInvisible.element.mouseout(function () {
		currentPaper.getById(polyline.element.id).attr({ "stroke": strokeColor });
	});

	_drawArrowHead(line, strokeColor, currentPaper);
}

function _drawArrowHead(line, color, currentPaper) {
	var doubleArrowWidth = 2 * ARROW_WIDTH;

	var arrowHead = currentPaper.path("M0 0L-" + (ARROW_WIDTH / 2 + .5) + " -" + doubleArrowWidth + "L" + (ARROW_WIDTH / 2 + .5) + " -" + doubleArrowWidth + "z");

	// anti smoothing
	if (this.strokeWidth % 2 == 1)
		line.x2 += .5, line.y2 += .5;

	arrowHead.transform("t" + line.x2 + "," + line.y2 + "");
	arrowHead.transform("...r" + Raphael.deg(line.angle - Math.PI / 2) + " " + 0 + " " + 0);

	arrowHead.attr("fill", color);

	arrowHead.attr("stroke-width", SEQUENCEFLOW_STROKE);
	arrowHead.attr("stroke", color);

	return arrowHead;
}
