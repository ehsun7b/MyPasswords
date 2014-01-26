$(function() {
  checkEngines();
});

function checkEngines() {
  $("#dlgEnginCheck").dialog({
    modal: true
  });

  $.ajax({
    url: "/database",
    type: "GET",
    success: function(data, textStatus, jqXHR) {
      if (data.successMessage) {
        alert(data.successMessage);
      }
      
      var select = $("form#frmLogin #engine");
      
      if (data.engines !== null && data.engines !== undefined) {
        for (var i = 0; i < data.engines.length; ++i) {
          var engine = data.engines[i];
          select.append($("<option/>", {value: engine.engineName}).append(engine.engineName));
        }
      }

      $("#dlgLogin").dialog({
        modal: true,
        width: "350px"
      });
      
    }
  }).done(function() {
    $("#dlgEnginCheck").dialog('close');
  });
}