/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var avgPriceChartData;
$.post('avgpricechart',(res)=>{
        avgPriceChartData = res;
        google.charts.load('current', {'packages':['bar']});
        google.charts.setOnLoadCallback(drawAvgPriceChart);
    })
function drawAvgPriceChart() {
    var data = google.visualization.arrayToDataTable(avgPriceChartData);
    var options = {
        title: "Density of Precious Metals, in g/cm^3",
        width: 600,
        height: 400,
        bar: {groupWidth: "95%"},
        legend: { position: "none" },
      };
    var chart = new google.charts.Bar(document.getElementById('avgPriceChart'));
    chart.draw(data, options);
  }

