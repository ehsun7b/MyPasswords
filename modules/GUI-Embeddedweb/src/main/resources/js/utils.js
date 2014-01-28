function showMessageOk(title, msg, okCallBack) {
  $("#dlgMessageOk").attr("title", title);
  $("#dlgMessageOk div#message").html(msg);
  $("#dlgMessageOk").dialog({
    modal: true,
    width: "350px",
    closeOnEscape: false,
    open: function(event, ui) {
      $(".ui-dialog-titlebar-close").hide();
    },
    buttons: {
      "OK": function() {
        $(this).dialog("close");
        okCallBack();
      }
    }
  });
}