/*

window.onpageshow = function(event) {
	console.log("reload()");
	
    if (event.persisted) {
        window.location.reload();
    }
};
*/

window.onpageshow = function(event) {
	console.log("reload()");
	
    if (event.persisted || window.performance && window.performance.navigation.type == 2) {
        window.location.reload();
    }
};