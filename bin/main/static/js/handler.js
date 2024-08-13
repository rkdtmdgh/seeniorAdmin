// input focus
export function setInputFocus(ele) {
	const input = document.querySelector(`input[name="${ele}"]`);
	if (input) {
		console.log('focus input:', input.name);
		input.focus();
	} else {
		console.log(`No input found with name: ${ele}`);
	}
}
