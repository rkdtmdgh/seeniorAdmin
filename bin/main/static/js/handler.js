// input focus
export function setInputFocus(ele) {
	const input = document.getElementById(ele);
	console.log('focus input:', input.name);
	input.focus();
}
