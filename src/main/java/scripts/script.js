const sideBarIcon = document.querySelector('.enlarge-siderbar');
const sideBar = document.querySelector('.sidebar');
let isVisible = false;

sideBarIcon.addEventListener('click', () => {

    if(!isVisible)
    {
        sideBar.style.width = "300px";
        sideBar.style.height = "100%";
        sideBar.style.display = "block";
        sideBar.style.left = "0";
        sideBarIcon.style.left = sideBar.offsetWidth + "px";

        isVisible = true;
    }
    else
    {
        sideBar.style.left = "-500px";
        sideBarIcon.style.left = "60px";

        isVisible = false;
    }

});



const addMoreButton = document.querySelector('#addMoreData');
let startForm = document.querySelector('#startForm');
let mainTextboxField = document.querySelector('#mainTextBox');

function setError(element, msg) {
    const parentElement = element.parentElement;
    const errorDiv = parentElement.querySelector('.error');

    errorDiv.textContent = msg;
    errorDiv.style.color = "#f73d39";
    errorDiv.style.padding = "2px";
    element.style.borderColor = "#f73d39";
}

function setSuccess(element) {
    const parent = element.parentElement;
    const errorDiv = parent.querySelector('.error');

    errorDiv.textContent = '';
    element.style.borderColor = "#198754";
    element.style.borderWidth = "3px";
}

function validateMainTextbox() {

    let errorCount = 0;

    let mainTextboxValue = mainTextboxField.value.trim();
    if(mainTextboxValue === '')
    {
        setError(mainTextboxField, "You must enter data before submitting.");
        errorCount++;
        return errorCount;
    }

    else
    {
        let splitted = mainTextboxValue.split("\n");
        let firstLine = splitted[0].split("=");

        if(splitted.length === 1)
        {
            setError(mainTextboxField,"You must enter at least one part.");
            errorCount++;
            return errorCount;
        }

        if(firstLine.length > 1)
        {
            setError(mainTextboxField, "You must enter a category name for this textbox.");
            errorCount++;
            return errorCount;
        }
        const regex = /^(.*?)(?:=|\-|:)\s*(\d+(?:\.\d{1,2})?)\s*(?:\$)?$/;
        for (let i = 1; i < splitted.length; i++)
        {
            let elem = splitted[i].trim();
            if (!regex.test(elem))
            {
                setError(mainTextboxField, "Every part must be followed by a price.");
                errorCount++;
                break;
            }
            else
            {
                setSuccess(mainTextboxField);
            }
        }
    }
    return errorCount;
}

startForm.addEventListener('submit', (event) => {
    event.preventDefault();

    if(validateMainTextbox() === 0)
    {
        console.log("Successful submission to server");
        const formData = new FormData(startForm);
        fetch('http://localhost:8081/submit', { method: 'POST', body: formData });
    }

});

function validateTextBox(textBox) {
    let errorCount = 0;

    let textBoxValue = textBox.value.trim();
    if (textBoxValue === '')
    {
        setError(textBox, "You must enter data before submitting.");
        errorCount++;
        return errorCount;
    }
    else
    {
        let splitted = textBoxValue.split("\n");
        let firstLine = splitted[0].split("=");

        if(splitted.length === 1)
        {
            setError(textBox,"You must enter at least one part.");
            errorCount++;
            return errorCount;
        }

        if (firstLine.length > 1)
        {
            setError(textBox, "You must enter a category name for this textbox.");
            errorCount++;
            return errorCount;
        }

        const regex = /^(.*?)(?:=|\-|:)\s*(\d+(?:\.\d{1,2})?)\s*(?:\$)?$/;
        for (let i = 1; i < splitted.length; i++)
        {
            let elem = splitted[i].trim();
            if (!regex.test(elem))
            {
                setError(textBox, "Every part must be followed by a price.");
                errorCount++;
                break;
            }
            else
            {
                setSuccess(textBox);
            }
        }
    }
    return errorCount;
}


let i = 1;
function addTextBox() {
    let textBox = document.createElement("textarea");
    textBox.placeholder = "Add more data";
    textBox.style.width = "750px";
    textBox.style.height = "450px";
    textBox.style.overflow = "auto";
    textBox.type = "text";
    textBox.className = "text-container";
    textBox.name = "textbox" + i;
    textBox.style.marginLeft = "155px";

    let textboxDiv = document.createElement("div");
    let errorDiv = document.createElement("div");
    textboxDiv.className = "textbox-class";
    errorDiv.className = "error";
    errorDiv.style.marginLeft = "155px";

    textboxDiv.appendChild(textBox);
    textboxDiv.appendChild(errorDiv);

    textboxDiv.style.display = "flex";
    textboxDiv.style.flexDirection = "column";

    let submitButton = document.createElement("button");
    submitButton.textContent = "Submit";
    submitButton.type = "submit";
    submitButton.className = "form-control";

    let addMoreDataButton = document.createElement("button");
    addMoreDataButton.textContent = "+ Add more data";
    addMoreDataButton.onclick = addTextBox;
    addMoreDataButton.type = "button";
    addMoreDataButton.className = "form-control";

    let cancelButton = document.createElement("button");
    cancelButton.textContent = " x Remove";
    cancelButton.className = "form-control";

    submitButton.style.backgroundColor = "#28a745";
    addMoreDataButton.style.backgroundColor = "rgba(0, 0, 255, 0.8)";
    cancelButton.style.backgroundColor = "#f54545";

    let newForm = document.createElement("form");
    let containerDiv = document.createElement("div");
    let btnClassDiv = document.createElement("div");
    let btnContainer1 = document.createElement("div");
    let btnContainer2 = document.createElement("div");
    let btnContainer3 = document.createElement("div");

    containerDiv.className = "container";
    btnClassDiv.className = "btn-class";
    btnContainer1.className = "btn-container";
    btnContainer2.className = "btn-container";
    btnContainer3.className = "btn-container";

    containerDiv.style.display = "flex";
    containerDiv.style.justifyContent = "center";
    containerDiv.style.alignItems = "center";
    containerDiv.style.marginBottom = "15px";


    btnClassDiv.style.display = "flex";
    btnClassDiv.style.marginLeft = "5px";


    textBox.style.backgroundColor = "#f7f2f2";
    textBox.style.border = " 2px solid rgba(0, 0, 0, 0.7)";
    textBox.style.resize = "none";
    textBox.style.fontFamily = "PT Bold Stars, sans-serif";
    // textBox.style.marginBottom = "50px";
    textBox.style.fontSize = "15px";


    newForm.appendChild(containerDiv);

    containerDiv.appendChild(textboxDiv);
    containerDiv.appendChild(btnClassDiv);

    btnClassDiv.appendChild(btnContainer1);
    btnClassDiv.appendChild(btnContainer2);
    btnClassDiv.appendChild(btnContainer3);

    btnContainer1.appendChild(submitButton);
    btnContainer2.appendChild(addMoreDataButton);
    btnContainer3.appendChild(cancelButton);

    document.body.appendChild(newForm);

    newForm.action = "http://localhost:8081/submit";
    newForm.method = "post";


    if (document.documentElement.scrollHeight > window.innerHeight)
    {
        textBox.scrollIntoView({behavior: 'smooth'});
    }

    cancelButton.onclick = () => {
        newForm.remove();
        form.remove();

    };

    i++;

    newForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const textContainer = newForm.querySelector('.text-container');
        if (validateTextBox(textContainer) === 0)
        {
            console.log("Successful submission to server");
            const formData = new FormData(newForm);
            fetch('http://localhost:8081/submit', { method: 'POST', body: formData });
        }
    });


    let textboxField = document.querySelector('.text-container');
}

addMoreButton.onclick = (event) => {
    event.preventDefault();
    addTextBox();

}