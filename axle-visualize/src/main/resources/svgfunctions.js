function init(evt) {
  if ( window.svgDocument == null ) {
    svgDocument = evt.target.ownerDocument;
  }
}

function ShowTooltip(evt, i) {
  tooltip = svgDocument.getElementById('tooltip' + i);
  //tooltip.setAttributeNS(null,"x",evt.clientX+10);
  //tooltip.setAttributeNS(null,"y",evt.clientY+30);
  tooltip.setAttributeNS(null,"visibility","visible");
}

function HideTooltip(evt, i) {
  tooltip = svgDocument.getElementById('tooltip' + i);
  tooltip.setAttributeNS(null,"visibility","hidden");
}
