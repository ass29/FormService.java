const form = document.getElementById("form");
const username = document.getElementById("username");
const password = document.getElementById("password");
const reEnterPassword = document.getElementById("reEnter-password");

username.addEventListener('blur', validateFields);
password.addEventListener('blur', validateFields);


form.addEventListener('submit', (event) => {
    event.preventDefault();

    validateFields();

});


function setError(element, errorMessage) {
    const parent = element.parentElement;
    const errorDiv = parent.querySelector('.error');

    errorDiv.textContent = errorMessage;
    errorDiv.style.color = "#f73d39";
    element.style.borderColor = "#f73d39";
    errorDiv.style.padding = "2px";

    if(invalidRegex)
    {
        errorDiv.style.fontSize = "15px";
    }
}

function setSuccess(element) {
    const parent = element.parentElement;
    const errorDiv = parent.querySelector('.error');

    errorDiv.textContent = '';
    element.style.borderColor = "#198754";
    element.style.borderWidth = "3px";

}

let invalidRegex = false;


function validateFields() {

    let successCount = 0;

    let usernameValue = username.value.trim();
    let passwordValue = password.value.trim();
    let reEnterPasswordValue = reEnterPassword.value.trim();

    if(usernameValue === '')
    {
        setError(username, "You must enter a username.");
    }
    else if(usernameValue.length < 8)
    {
        setError(username, "Username must be longer than 8 characters.");
    }
    else
    {
        setSuccess(username);
        successCount++;
    }

    const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{};:,<.>]).+$/;
    if(passwordValue === '')
    {
        setError(password, "You must enter a password.");
    }
    else if(passwordValue.length < 8)
    {
        setError(password, "Password must be longer than 8 characters.");
    }
    else if(!(regex.test(passwordValue)))
    {
        setError(password, "Password must contain at least one capital letter, one digit, and one special character.");
        invalidRegex = true;
    }
    else
    {
        setSuccess(password);
        successCount++;
    }

    if(reEnterPasswordValue === '')
    {
        setError(reEnterPassword, "You must re-enter your password.");
    }
    else if(reEnterPasswordValue !== passwordValue)
    {
        setError(reEnterPassword, "Passwords do not match.")
    }
    else
    {
        setSuccess(reEnterPassword);
        successCount++;
    }

    if(successCount === 3)
    {
        form.submit();
    }

}











