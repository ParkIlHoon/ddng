var comboChartTheme = {
    series: {
        pie1: {
            colors: ['#00a9ff', '#ffb840', '#ff5a46', '#00bd9f', '#785fff', '#f28b8c', '#989486', '#516f7d', '#29dbe3', '#dddddd'],
            label: {
                color: '#000000',
                fontFamily: 'Noto Sans',
                fontSize:18
            }
        },
        pie2: {
            colors: ['#00a9ff', '#ffb840', '#ff5a46', '#00bd9f', '#785fff', '#f28b8c', '#989486', '#516f7d', '#29dbe3', '#dddddd'],
            label: {
                color: '#fff',
                fontFamily: 'Noto Sans',
                fontSize:18
            }
        }
    }
};
tui.chart.registerTheme('comboChartTheme', comboChartTheme);