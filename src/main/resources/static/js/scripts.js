/*!
    * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2023 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});

// JavaScript function to toggle the visibility of passwords
function togglePasswordVisibility() {
    var passwordField = document.getElementById("password");
    var passwordVisibilityButton = document.getElementById("passwordVisibilityButton");

    if (passwordField.type === "password") {
        passwordField.type = "text";
        passwordVisibilityButton.classList.remove("fa-eye");
        passwordVisibilityButton.classList.add("fa-eye-slash");
    } else {
        passwordField.type = "password";
        passwordVisibilityButton.classList.remove("fa-eye-slash");
        passwordVisibilityButton.classList.add("fa-eye");
    }
}

function togglePasswordConfirmVisibility() {
    var confirmPasswordField = document.getElementById("confirmPassword");
    var confirmPasswordVisibilityButton = document.getElementById("confirmPasswordVisibilityButton");

    if (confirmPasswordField.type === "password") {
        confirmPasswordField.type = "text";
        confirmPasswordVisibilityButton.classList.remove("fa-eye");
        confirmPasswordVisibilityButton.classList.add("fa-eye-slash");
    } else {
        confirmPasswordField.type = "password";
        confirmPasswordVisibilityButton.classList.remove("fa-eye-slash");
        confirmPasswordVisibilityButton.classList.add("fa-eye");
    }
}

function togglePasswordVisibilityr() {
    var passwordField = document.getElementById("inputPassword");
    var passwordVisibilityButton = document.getElementById("passwordVisibilityButton");

    if (passwordField.type === "password") {
        passwordField.type = "text";
        passwordVisibilityButton.classList.remove("fa-eye");
        passwordVisibilityButton.classList.add("fa-eye-slash");
    } else {
        passwordField.type = "password";
        passwordVisibilityButton.classList.remove("fa-eye-slash");
        passwordVisibilityButton.classList.add("fa-eye");
    }
}

function togglePasswordConfirmVisibilityr() {
    var confirmPasswordField = document.getElementById("inputPasswordConfirm");
    var confirmPasswordVisibilityButton = document.getElementById("confirmPasswordVisibilityButton");

    if (confirmPasswordField.type === "password") {
        confirmPasswordField.type = "text";
        confirmPasswordVisibilityButton.classList.remove("fa-eye");
        confirmPasswordVisibilityButton.classList.add("fa-eye-slash");
    } else {
        confirmPasswordField.type = "password";
        confirmPasswordVisibilityButton.classList.remove("fa-eye-slash");
        confirmPasswordVisibilityButton.classList.add("fa-eye");
    }
}

