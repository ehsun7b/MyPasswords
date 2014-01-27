$(function() {
  checkEngines();
});

function checkEngines() {
  $("#dlgEnginCheck").dialog({
    modal: true,
    closeOnEscape: false,
    open: function(event, ui) {
      $(".ui-dialog-titlebar-close").hide();
    }
  });

  $.ajax({
    url: "/database",
    type: "GET",
    success: function(data, textStatus, jqXHR) {
      if (data.success) {
        var engines = data.engines;
        if (data.successMessage) {
          showMessageOk("Welcome", data.successMessage, function() {
            showLogin(engines);
          });          
        } else {
          showLogin(engines);
        }
      }
    }
  }).done(function() {
    $("#dlgEnginCheck").dialog('close');
  });
}

function showLogin(engines) {
  var select = $("form#frmLogin #engine");

  if (engines !== null && engines !== undefined) {
    for (var i = 0; i < engines.length; ++i) {
      var engine = engines[i];
      select.append($("<option/>", {value: engine.engineName}).append(engine.engineName));
    }
  }

  $("#dlgLogin").dialog({
    modal: true,
    width: "350px",
    closeOnEscape: false,
    open: function(event, ui) {
      $(".ui-dialog-titlebar-close").hide();
    },
  });
}