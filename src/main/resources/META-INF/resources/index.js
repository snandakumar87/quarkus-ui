if (!!window.EventSource) {
    var eventSource = new EventSource("/stream");
    eventSource.onmessage = function(event) {
        data = JSON.parse(event.data);

        var row = '<tr><td>' + data.id + '</td><td>' + data.amount + '</td><td>' + data.country + '</td><td>' + data.merchantId + '</td><td>';

        $('#tbody').append(row);
    };
} else {
    window.alert("EventSource not available on this browser.")
}


