// https://stackoverflow.com/a/6234804
function escapeHtml(s) {
	return s
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

function sortTrimLevels(a,b) {
    var nameA = a.name.toLowerCase();
    var nameB = b.name.toLowerCase();

    if (nameA < nameB) return -1;
    if (nameA > nameB) return 1;

    return 0;
}

function addNewTrimLevelInput() {
    $("#trimlevels").append("<div class='input-group'>" +
                              "<input class='form-control trimlevel' type='text'/>" +
                            "</div>");
}

