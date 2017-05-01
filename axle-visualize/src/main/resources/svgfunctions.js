
function init(evt) {
  if ( window.svgDocument == null ) {
    svgDocument = evt.target.ownerDocument;
  }
}

function ShowTooltip(evt, i) {
  tooltipbg = svgDocument.getElementById('tooltipbg' + i);
  tooltiptext = svgDocument.getElementById('tooltiptext' + i);
  tbb = tooltiptext.getBBox();
  tooltipbg.setAttributeNS(null,"visibility","visible");
  tooltipbg.setAttributeNS(null,"width",tbb.width);
  tooltipbg.setAttributeNS(null,"height",tbb.height);
  tooltipbg.setAttributeNS(null,"x",tbb.x);
  tooltipbg.setAttributeNS(null,"y",tbb.y);
  tooltiptext.setAttributeNS(null,"visibility","visible");
}

function HideTooltip(evt, i) {
  tooltipbg = svgDocument.getElementById('tooltipbg' + i);
  tooltiptext = svgDocument.getElementById('tooltiptext' + i);
  tooltipbg.setAttributeNS(null,"visibility","hidden");
  tooltiptext.setAttributeNS(null,"visibility","hidden");
}
