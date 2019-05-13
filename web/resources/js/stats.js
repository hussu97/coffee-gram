var avgPriceChartData;
    $.post('avgpricechart', (res) => {
      avgPriceChartData = JSON.parse(res);
      google.charts.load('current', {
        'packages': ['bar']
      });
      google.charts.setOnLoadCallback(drawAvgPriceChart);
    })
    drawAvgPriceChart = () => {
      var data = google.visualization.arrayToDataTable(avgPriceChartData);
      var options = {
        chart:{
            title: "Average Price of Coffee per Location",
        },
        axes: {
            x: {
              0: { side: 'top', label: 'Locations'} // Top x-axis.
            }
          },
        legend: {
          position: "none"
        },
      };
      var chart = new google.charts.Bar(document.getElementById('avgPriceChart'));
      chart.draw(data, options);
    }
    var photosPerLocationData;
    $.post('photoperlocchart', (res) => {
      photosPerLocationData = JSON.parse(res);
      google.charts.load('current', {
        'packages': ['corechart']
      });
      google.charts.setOnLoadCallback(drawPhotoPerLocChart);
    })
    drawPhotoPerLocChart = () => {
      var data = google.visualization.arrayToDataTable(photosPerLocationData);
      var options = {
        title: "Photos Per Location",
        is3D: true
      };
      var chart = new google.visualization.PieChart(document.getElementById('photoPerLocChart'));
      chart.draw(data, options);
    }
    var postsOverTimeChart;
    $.post('postsovertimechart', (res) => {
      postsOverTimeChart = JSON.parse(res);
      console.log(postsOverTimeChart)
      google.charts.load('current', {
        'packages': ['corechart']
      });
      google.charts.setOnLoadCallback(drawPostsOverTimeChart);
    })
    drawPostsOverTimeChart = () => {
      var data = google.visualization.arrayToDataTable(postsOverTimeChart);
      var options = {
          title : 'Posts created over time',
          vAxis: {title: 'Posts'},
          hAxis: {title: 'Date'},
          seriesType: 'bars',
          series: {1: {type: 'line'}},
          legend: {
          position: "none"
        },
        };
      var chart = new google.visualization.ComboChart(document.getElementById('postsOverTimeChart'));
      chart.draw(data, options);
    }
    var usersOverTimeChart;
    $.post('usersovertimechart', (res) => {
      usersOverTimeChart = JSON.parse(res);
      google.charts.load('current', {
        'packages': ['bar']
      });
      google.charts.setOnLoadCallback(drawUsersOverTimeChart);
    })
    drawUsersOverTimeChart = () => {
      var data = google.visualization.arrayToDataTable(usersOverTimeChart);
      var options = {
          title : 'Users registered over time',
          vAxis: {title: 'Users'},
          hAxis: {title: 'Date'},
          seriesType: 'bars',
          series: {1: {type: 'line'}},
          legend: {
          position: "none"
        },
        };
      var chart = new google.visualization.ComboChart(document.getElementById('usersOverTimeChart'));
      chart.draw(data, options);
    }