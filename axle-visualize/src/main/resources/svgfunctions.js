
function init(evt) {
  if ( window.svgDocument == null ) {
    svgDocument = evt.target.ownerDocument;
  }
}

function ShowTooltip(evt, i) {
  tooltiptext = svgDocument.getElementById('tooltiptext' + i);
  tooltipbg = svgDocument.getElementById('tooltipbg' + i);
  if( tooltipbg != null) {
    tbb = tooltiptext.getBBox();
    tooltipbg.setAttributeNS(null, "width", tbb.width);
    tooltipbg.setAttributeNS(null, "height", tbb.height);
    tooltipbg.setAttributeNS(null, "x", tbb.x);
    tooltipbg.setAttributeNS(null, "y", tbb.y);
    tooltipbg.setAttributeNS(null, "visibility", "visible");
  }
  tooltiptext.setAttributeNS(null,"visibility","visible");
}

function HideTooltip(evt, i) {
  tooltiptext = svgDocument.getElementById('tooltiptext' + i);
  tooltiptext.setAttributeNS(null,"visibility","hidden");
  tooltipbg = svgDocument.getElementById('tooltipbg' + i);
  if( tooltipbg != null ) {
    tooltipbg.setAttributeNS(null,"visibility","hidden");
  }
}
