/**
 * Created by NamNH on 6/25/2016.
 * http://jsfiddle.net/76dLm8h7/3/
 * http://jsfiddle.net/fwxbfu1u/6/
 * http://jsfiddle.net/jNc8E/
 * 
 * http://jsfiddle.net/r8d3jnx6/1/ -> custom label
 * http://jsfiddle.net/xhfgzfps/1/
 */
var GRAPH_HEIGHT = 200;
var COLOR_DOWNLOAD = '#6AA4D9';
var COLOR_UPLOAD = '#ED7D31';
var COLOR_LATENCY = '#A5A5A5';

var speedConfig = {
		options: {
	        chart: {
	            type: 'gauge',
	            plotBackgroundColor: null,
	            plotBackgroundImage: null,
	            plotBorderWidth: 0,
	            plotShadow: false,
	            animation: {
	                duration: 500,
	                easing: 'easeOutBounce'
	            },
	        },
	        title: {
	            text: 'Speedometer'
	        },
	        pane: {
	            startAngle: -150,
	            endAngle: 150,
	            background: null, 
	        },
//	        tooltip: {
//                shared: true,
//                formatter: function(){
//                    return 'aaaa';
//                },
//                useHtml: true
//            },
	        credits: false
	    },
	    yAxis: {
	        min: 0,
	        max: 100,
	        tickPositions: [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100],
//	        tickPositions: [0, 10 * MBPS_TO_KBPS, 20 * MBPS_TO_KBPS , 30 * MBPS_TO_KBPS , 40 * MBPS_TO_KBPS 
//	                        , 50 * MBPS_TO_KBPS , 60 * MBPS_TO_KBPS , 70 * MBPS_TO_KBPS , 80 * MBPS_TO_KBPS 
//	                        , 90 * MBPS_TO_KBPS , 100 * MBPS_TO_KBPS ],
	        minorTickInterval: 'auto',
	        minorTickWidth: 1,
	        minorTickLength: 10,
	        //minorTickPosition: 'outside',
	        //minorTickColor: '#666',
	        minorTickColor: '#c4c4c4',

	        tickPixelInterval: 30,
	        tickWidth: 2,
	        //tickPosition: 'inside',
	        tickLength: 15,
	        //tickColor: '#666',
	        tickColor: '#c4c4c4',
	        labels: {
	            //step: 2,
	            //rotation: 'auto',
	            distance: -25,
	        },
	        title: {
	            text: 'Mbps'
	        },
	        plotBands: [{
	            from: 0,
	            to: 40,
	            //color: '#55BF3B' // green
	            color: '#c4c4c4'
	        }, {
	            from: 40,
	            to: 80,
	            //color: '#DDDF0D' // yellow
	            color: '#c4c4c4'
	        }, {
	            from: 80,
	            to: 100,
	            //color: '#DF5353' // red
	            color: '#c4c4c4'
	        }]        
	    },

	    series: [{
	        name: 'Speed',
	        data: [0],
	        dataLabels: {
//                borderRadius: 5,
//                backgroundColor: "rgba(244,109,67, 0.7)",
//                borderWidth: 1,
                borderColor: null,
//                style: {
//                    fontSize: "30px"
//                },
//                color: "white",
                crop: false,
                overflow: "none",
                formatter: function () {
                	var s = "";
                	if (this.point.y != undefined) {
                		s = Highcharts.numberFormat(this.point.y,2);
                	}
                    return s;
                },
                y: 110,
                zIndex: 10
            },
//	        tooltip: {
//	        	useHTML: true,
//	        	pointFormat : 'Speed test',
//                shared: true
//	        },
	        dial: {
	            backgroundColor : '#c4c4c4'
	        	//backgroundColor : 'black'
	        },
	        pivot: {
	            backgroundColor: '#c4c4c4'
	        	//backgroundColor: 'black'
	        }
	    }],
	};

var graphConfig = {
    options: {
        chart: {
            type: 'spline',
            marginRight: 10,
            // Explicitly tell the width and height of a chart
        	width: null,
        	height: GRAPH_HEIGHT
        },
        credits: false,
//        tooltip: {
//            shared: true,
//            formatter: function(){
//                return 'aaaa';
//            },
//            useHtml: true
//        },
    },
    series: [{
    	name: 'Download',
        data: [],
        marker: {
            enabled: false
        },
        color: '#3747F0',
        tooltip: {
//        	dateTimeLabelFormats: {
//                hour: '%S.%L'
//            },
        	xDateFormat: '%M:%S.%L',
//        	headerFormat: 'Time: {point.x:%S.%L}</br>',//Highcharts.dateFormat('%S.%L', point.x),
//            pointFormat: '{point.x:%e. %b}: {point.y:.2f} m'
        	useHTML: false,
        	pointFormat : '{series.name}: <b>{point.y:.2f}</b>',
            shared: true
        },
    },
    {
    	name: 'Upload',
        data: [],
        marker: {
            enabled: false
        },
        tooltip: {
        	xDateFormat: '%M:%S.%L',
        	useHTML: true,
        	pointFormat : '{series.name}: <b>{point.y:.2f}</b>',
            shared: true
        },
    }],
    title: {
        text: 'Bandwidth'
    },
    xAxis: {
        type: 'datetime',
        labels: {
            enabled:false
        }
//	    dateTimeLabelFormats: { // don't display the dummy year
//	        month: '%e. %b',
//	        year: '%b'
//	    },
//	    title: {
//	        text: 'Date'
//	    }
    },
    yAxis: {
        title: {
            text: 'Mbps'
        },
//        min: 1,
//    	max: 100,
    },
};

var latencyGraphConfig = {
    options: {
        chart: {
            type: 'column',
            marginRight: 10,
            // Explicitly tell the width and height of a chart
        	width: null,
        	height: GRAPH_HEIGHT
        },
        credits: false
    },
    series: [
    {
    	name: 'Latency',
    	color:'#5D95A8',
        data: [],
        tooltip: {
            valueSuffix: ' ms'
        }
    }],
    title: {
        text: 'Latency'
    },
    xAxis: {
//        type: 'datetime',
    	labels: {
            enabled:false
        }
    },
    yAxis: {
        title: {
            text: 'ms'
        },
        plotLines: [{
            value: 0,
            width: 1,
            color: '#808080'
        }]
    },
    tooltip: {
        formatter: function () {
            return '<b>' + this.series.name + '</b><br/>' +
                'Sample #' + this.x + '<br/>' +
                Highcharts.numberFormat(this.y, 2) + ' ms';
        }
    },
};

var graphHistoryConfigMbps = {
        options: {
            chart: {
                zoomType: 'xy'
            },
            title: {
                text: 'Speed test history data'
            },
            xAxis: [{
                categories: []
            }],
            yAxis: [{ // Secondary yAxis
                gridLineWidth: 0,
                title: {
                    text: 'Speed rate',
                    style: {
                        color: '#4572A7'
                    }
                },
                labels: {
                    formatter: function () {
                        return this.value + ' Mbps';
                    },
                    style: {
                        color: '#4572A7'
                    }
                }

            }, { // Tertiary yAxis
                gridLineWidth: 0,
                title: {
                    text: 'Latency',
                    style: {
                        color: '#AA4643'
                    }
                },
                labels: {
                    formatter: function () {
                        return this.value + ' ms';
                    },
                    style: {
                        color: '#AA4643'
                    }
                },
                opposite: true
            }],
            tooltip: {
                shared: true
            },
            credits: false
        },
        series: [
		{
		    name: 'Latency',
		    color: COLOR_LATENCY,
		    type: 'column',
		    yAxis: 1,
		    data: [],
		    pointWidth: 15,
		    tooltip: {
		        valueSuffix: ' ms'
		    }
		}, {
		    name: 'Download',
		    color: COLOR_DOWNLOAD,
		    type: 'line',
		    data: [],
		    tooltip: {
		        valueSuffix: ' Mbps'
		    }
		
		}, {
            name: 'Upload',
            color: COLOR_UPLOAD,
            type: 'line',
            data: [],
            tooltip: {
                valueSuffix: ' Mbps'
            }

        }]
    };

var graphHistoryConfigKbps = {
        options: {
            chart: {
                zoomType: 'xy'
            },
            title: {
                text: 'Speed test history data'
            },
            xAxis: [{
                categories: []
            }],
            yAxis: [{ // Secondary yAxis
                gridLineWidth: 0,
                title: {
                    text: 'Speed rate',
                    style: {
                        color: '#4572A7'
                    }
                },
                labels: {
                    formatter: function () {
                        return this.value + ' Kbps';
                    },
                    style: {
                        color: '#4572A7'
                    }
                }

            }, { // Tertiary yAxis
                gridLineWidth: 0,
                title: {
                    text: 'Latency',
                    style: {
                        color: '#AA4643'
                    }
                },
                labels: {
                    formatter: function () {
                        return this.value + ' ms';
                    },
                    style: {
                        color: '#AA4643'
                    }
                },
                opposite: true
            }],
            tooltip: {
                shared: true
            },
            credits: false
        },
        series: [
		{
		    name: 'Latency',
		    color: COLOR_LATENCY,
		    type: 'column',
		    yAxis: 1,
		    data: [],
		    pointWidth: 15,
		    tooltip: {
		        valueSuffix: ' ms'
		    }
		}, {
		    name: 'Download',
		    color: COLOR_DOWNLOAD,
		    type: 'line',
		    data: [],
		    tooltip: {
		        valueSuffix: ' Kbps'
		    }
		
		}, {
            name: 'Upload',
            color: COLOR_UPLOAD,
            type: 'line',
            data: [],
            tooltip: {
                valueSuffix: ' Kbps'
            }

        }]
    };
