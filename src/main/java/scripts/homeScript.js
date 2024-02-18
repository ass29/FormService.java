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