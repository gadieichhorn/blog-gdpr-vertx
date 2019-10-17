$( document ).ready(function() {
    console.log( "ready!" );

    var cards = document.getElementById('chat-cards');
	var eb = new EventBus("/eventbus/");

	eb.onopen = function () {
		eb.registerHandler("chat-service-outbound", function (err, msg) {
			console.log(msg);

            var message = JSON.parse(msg.body);
			console.log(message);

            let card = document.createElement('div');
            card.className = 'card p-1';

            let cardBody = document.createElement('div');
            cardBody.className = 'card-body';

            let title = document.createElement('h5');
            title.innerText = message.from;
            title.className = 'card-title';

            let text = document.createElement('p');
            text.innerText = message.message;
            text.className = 'card-text';

            cardBody.appendChild(title);
            cardBody.appendChild(text);
            card.appendChild(cardBody);

            cards.appendChild(card);

		});
	};

	$('#submit').on('click', function(event) {
		event.preventDefault(); // To prevent following the link (optional)
		var message = $('#input').val();

		if (message.length > 0) {
			var myObj = {from: "tim", message: message};
			console.log(myObj);
			eb.publish("chat-service-inbound", myObj);
			$('#input').val("");
		}
	});

});
