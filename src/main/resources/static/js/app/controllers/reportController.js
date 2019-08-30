/**
 * Controller for report functions
 */

webShopApp.controller('reportController', function($scope, $window, $timeout, ordersFactory) 
{
	const prepareChart = function(chart)
	{
		chart.dateFormatter.inputDateFormat = "yyyy-MM-dd";

		var dateAxis = chart.xAxes.push(new am4charts.DateAxis());
		var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());

		var series = chart.series.push(new am4charts.LineSeries());
		series.dataFields.valueY = "value";
		series.dataFields.dateX = "date";
		series.tooltipText = "{value}"
		series.strokeWidth = 2;
		series.minBulletDistance = 15;

		series.tooltip.background.cornerRadius = 20;
		series.tooltip.background.strokeOpacity = 0;
		series.tooltip.pointerOrientation = "vertical";
		series.tooltip.label.minWidth = 40;
		series.tooltip.label.minHeight = 40;
		series.tooltip.label.textAlign = "middle";
		series.tooltip.label.textValign = "middle";

		var bullet = series.bullets.push(new am4charts.CircleBullet());
		bullet.circle.strokeWidth = 2;
		bullet.circle.radius = 4;
		bullet.circle.fill = am4core.color("#fff");

		var bullethover = bullet.states.create("hover");
		bullethover.properties.scale = 1.3;

		// Make a panning cursor
		chart.cursor = new am4charts.XYCursor();
		chart.cursor.behavior = "panXY";
		chart.cursor.xAxis = dateAxis;
		chart.cursor.snapToSeries = series;

		// Create vertical scrollbar and place it before the value axis
		chart.scrollbarY = new am4core.Scrollbar();
		chart.scrollbarY.parent = chart.leftAxesContainer;
		chart.scrollbarY.toBack();

		// Create a horizontal scrollbar with previe and place it underneath the date axis
		chart.scrollbarX = new am4charts.XYChartScrollbar();
		chart.scrollbarX.series.push(series);
		chart.scrollbarX.parent = chart.bottomAxesContainer;

		chart.events.on("ready", function () {
		  dateAxis.zoom({start:0.79, end:1});
		});
	}
	
	const generateChart = function(chartId, data)
	{
		am4core.ready(function() {

			am4core.useTheme(am4themes_kelly);
			am4core.useTheme(am4themes_animated);

			let chart = am4core.create(chartId, am4charts.XYChart);
			chart.data = data;
			prepareChart(chart);
		});
	}
	
	$scope.generateReport = function(startDate, endDate)
	{
		if (!startDate || !endDate)
		{
			error = {};
			error.data = {};
			error.data.message = 'You must choose both start and end date!';
			$scope.displayFailureMessage(error);
			return;
		}
		
		startDate = new Date(startDate).toISOString().slice(0, 10);
		endDate = new Date(endDate).toISOString().slice(0, 10);
		
		ordersFactory.getIncomeInPeriod(startDate, endDate).then(function(data)
		{
			$scope.incomeReports = data.data;
			if ($scope.incomeReports.length > 0)
			{
				$scope.totalIncome = 0;
				for (let incomeReport of $scope.incomeReports)
				{
					$scope.totalIncome += incomeReport.value;
				}
				
				generateChart('incomeChart', $scope.incomeReports);
			}
		})
		.catch(function(error) 
		{
			$scope.displayFailureMessage(error);
		});
		
		ordersFactory.getNumberOfCanceledOrdersInPeriod(startDate, endDate).then(function(data)
		{
			$scope.canceledOrdersReports = data.data;
			if ($scope.canceledOrdersReports.length > 0)
			{
				$scope.totalCancellations = 0;
				for (let canceledOrdersReport of $scope.canceledOrdersReports)
				{
					$scope.totalCancellations += canceledOrdersReport.value;
				}
				generateChart('canceledOrdersChart', $scope.canceledOrdersReports);
			}
		})
		.catch(function(error) 
		{
			$scope.displayFailureMessage(error);
		});
		
		ordersFactory.getDeliveredOrdersInPeriod(startDate, endDate).then(function(data)
		{
			$scope.deliveredOrders = data.data;
			$scope.deliveredOrders.sort((a,b) => b.timestamp.localeCompare(a.timestamp));
		})
		.catch(function(error) 
		{
			$scope.displayFailureMessage(error);
		});
	}
});