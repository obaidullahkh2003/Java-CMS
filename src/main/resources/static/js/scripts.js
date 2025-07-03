//temeplet.html script
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

//login.html script
document.addEventListener('DOMContentLoaded', function() {
    const togglePassword = document.querySelector('#togglePassword');
    const password = document.querySelector('#inputPassword');
    const icon = togglePassword.querySelector('i');

    togglePassword.addEventListener('click', function(e) {
        e.preventDefault();
        // Toggle the type attribute
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);

        // Toggle the eye icon
        icon.classList.toggle('fa-eye-slash');
        icon.classList.toggle('fa-eye');
    });
});

//register.html script

document.addEventListener('DOMContentLoaded', function() {
    // Toggle main password visibility
    const togglePassword = document.querySelector('#togglePassword');
    const password = document.querySelector('#exampleInputPassword');
    const passwordIcon = togglePassword.querySelector('i');

    togglePassword.addEventListener('click', function(e) {
        e.preventDefault();
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        passwordIcon.classList.toggle('fa-eye-slash');
        passwordIcon.classList.toggle('fa-eye');
    });

    // Toggle confirm password visibility
    const toggleConfirmPassword = document.querySelector('#toggleConfirmPassword');
    const confirmPassword = document.querySelector('#exampleRepeatPassword');
    const confirmIcon = toggleConfirmPassword.querySelector('i');

    toggleConfirmPassword.addEventListener('click', function(e) {
        e.preventDefault();
        const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
        confirmPassword.setAttribute('type', type);
        confirmIcon.classList.toggle('fa-eye-slash');
        confirmIcon.classList.toggle('fa-eye');
    });
});

// password.html script

$(document).ready(function() {
    // Initialize Bootstrap modals
    const successModal = new bootstrap.Modal(document.getElementById('successModal'));
    const errorModal = new bootstrap.Modal(document.getElementById('errorModal'));

    $('#forgotPasswordForm').on('submit', function(e) {
        e.preventDefault();
        const email = $('#inputEmail').val().trim();

        // Validate email format
        if (!isValidEmail(email)) {
            showError('Invalid Email', 'Please enter a valid email address.');
            return;
        }

        // Show loading state
        const submitBtn = $(this).find('button[type="submit"]');
        setLoadingState(submitBtn, true);

        $.ajax({
            url: `/forgotPassword/verifyMail/${email}`,
            type: 'POST',
            contentType: 'application/json',
            success: function(response) {
                if (response && response.message) {
                    // Redirect to verify-otp page with email parameter
                    window.location.href = `/api/auth/verify-otp?email=${encodeURIComponent(email)}`;
                } else {
                    showError('Unexpected Response', 'Server returned an unexpected response.');
                }
            },
            error: function(xhr) {
                let errorTitle = 'Request Failed';
                let errorMessage = 'An error occurred while processing your request';

                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (errorResponse.error) {
                        errorMessage = errorResponse.error;
                    }
                    if (errorResponse.message) {
                        errorMessage = errorResponse.message;
                    }
                    if (xhr.status === 404) {
                        errorTitle = 'Email Not Found';
                    }
                } catch (e) {
                    if (xhr.responseText) {
                        errorMessage = xhr.responseText;
                    }
                }

                showError(errorTitle, errorMessage);
            },
            complete: function() {
                setLoadingState(submitBtn, false);
            }
        });
    });

    function isValidEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    function setLoadingState(button, isLoading) {
        if (isLoading) {
            button.prop('disabled', true);
            button.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Sending...');
        } else {
            button.prop('disabled', false);
            button.text('Reset Password');
        }
    }

    function showError(title, message) {
        $('#errorModalLabel').text(title);
        $('#errorModalBody').text(message);
        errorModal.show();
    }
});

// otp script

$(document).ready(function() {
    // Initialize Bootstrap modals
    const successModal = new bootstrap.Modal(document.getElementById('successModal'));
    const errorModal = new bootstrap.Modal(document.getElementById('errorModal'));

    // Resend OTP timer
    let resendTimer = 60;
    const resendOtpLink = $('#resendOtpLink');
    const resendTimerEl = $('#resendTimer');
    const countdownEl = $('#countdown');

    // Start the resend timer
    startResendTimer();

    // OTP input handling
    $('#otp').on('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
        if (this.value.length === 6) {
            $('#verifyOtpForm').submit();
        }
    }).on('paste', function(e) {
        const pasteData = e.originalEvent.clipboardData.getData('text');
        const numericData = pasteData.replace(/[^0-9]/g, '');
        if (numericData.length === 6) {
            $(this).val(numericData);
            $('#verifyOtpForm').submit();
        }
    });

    // Form submission
    $('#verifyOtpForm').on('submit', function(e) {
        e.preventDefault();
        const otp = $('#otp').val().trim();
        const email = $('#email').val();

        // Validate OTP format
        if (!/^\d{6}$/.test(otp)) {
            showModal('error', 'Invalid OTP Format', 'Please enter exactly 6 digits');
            return;
        }

        // Show loading state
        const submitBtn = $(this).find('button[type="submit"]');
        setLoadingState(submitBtn, true);

        $.ajax({
            url: `/forgotPassword/verifyOtp/${otp}/${email}`,
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            success: function(response) {
                if (response?.status === "success" && response.token) {
                    sessionStorage.setItem('passwordResetToken', response.token);
                    showModal('success', 'Success', 'OTP verified successfully! Redirecting...');
                    setTimeout(() => {
                        window.location.href = `/api/auth/reset-password?email=${encodeURIComponent(email)}&token=${encodeURIComponent(response.token)}`;
                    }, 1500);
                } else {
                    showModal('error',
                        'Verification Failed',
                        response?.message || 'Invalid OTP',
                        response?.details || '');
                }
            },
            error: function(xhr) {
                let errorTitle = 'Verification Failed';
                let errorMessage = 'An error occurred while verifying OTP';

                if (xhr.status === 0) {
                    errorMessage = 'Network error - please check your connection';
                } else if (xhr.status === 429) {
                    errorTitle = 'Too Many Requests';
                    errorMessage = 'Please wait before trying again';
                } else {
                    try {
                        const errorResponse = xhr.responseJSON || JSON.parse(xhr.responseText);
                        errorTitle = errorResponse.error || errorTitle;
                        errorMessage = errorResponse.message || errorMessage;
                    } catch (e) {
                        errorMessage = xhr.responseText || errorMessage;
                    }
                }

                showModal('error', errorTitle, errorMessage);
            },
            complete: function() {
                setLoadingState(submitBtn, false);
            }
        });
    });

    // Resend OTP handler
    resendOtpLink.on('click', function(e) {
        e.preventDefault();
        if (resendTimer > 0) return;

        const email = $('#email').val();
        setLoadingState(resendOtpLink, true);

        $.ajax({
            url: `/forgotPassword/verifyMail/${email}`,
            type: 'POST',
            dataType: 'json',
            success: function(response) {
                if (response?.status === "success") {
                    showModal('success', 'OTP Resent', 'A new OTP has been sent to your email');
                    startResendTimer();
                } else {
                    showModal('error', 'Resend Failed', response?.message || 'Failed to resend OTP');
                }
            },
            error: function(xhr) {
                let errorMessage = 'Failed to resend OTP. Please try again.';
                try {
                    const errorResponse = xhr.responseJSON || JSON.parse(xhr.responseText);
                    errorMessage = errorResponse.message || errorResponse.error || errorMessage;
                } catch (e) {
                    errorMessage = xhr.responseText || errorMessage;
                }
                showModal('error', 'Resend Failed', errorMessage);
            },
            complete: function() {
                setLoadingState(resendOtpLink, false);
                resendOtpLink.text('Resend OTP');
            }
        });
    });

    // Helper functions
    function setLoadingState(element, isLoading) {
        const text = element.text().replace(/^[^a-zA-Z]+/, '');
        element.prop('disabled', isLoading);
        element.html(isLoading
            ? `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ${text}`
            : text);
    }

    function showModal(type, title, message, details = '') {
        const modal = type === 'success' ? successModal : errorModal;
        const titleEl = type === 'success' ? '#successModalLabel' : '#errorModalLabel';
        const messageEl = type === 'success' ? '#successModal .modal-body' : '#errorMessage';

        $(titleEl).text(title);
        $(messageEl).html(type === 'success'
            ? `<i class="fas fa-check-circle me-2"></i> ${message}`
            : message);

        if (type === 'error' && details) {
            $('#errorDetails').html(details).show();
        } else {
            $('#errorDetails').hide();
        }

        modal.show();
    }

    function startResendTimer() {
        resendTimer = 60;
        resendOtpLink.addClass('disabled');
        resendTimerEl.removeClass('d-none');

        const timerInterval = setInterval(() => {
            resendTimer--;
            countdownEl.text(resendTimer);

            if (resendTimer <= 0) {
                clearInterval(timerInterval);
                resendOtpLink.removeClass('disabled');
                resendTimerEl.addClass('d-none');
            }
        }, 1000);
    }
});

// change password scrpt

$(document).ready(function() {
    // Initialize Bootstrap modals
    const successModal = new bootstrap.Modal(document.getElementById('successModal'));
    const errorModal = new bootstrap.Modal(document.getElementById('errorModal'));

    // Password toggle functionality
    function setupPasswordToggle(toggleId, inputId) {
        $(`#${toggleId}`).click(function() {
            const input = $(`#${inputId}`);
            const icon = $(this).find('i');

            if (input.attr('type') === 'password') {
                input.attr('type', 'text');
                icon.removeClass('fa-eye-slash').addClass('fa-eye');
            } else {
                input.attr('type', 'password');
                icon.removeClass('fa-eye').addClass('fa-eye-slash');
            }
        });
    }

    // Set up toggles for both password fields
    setupPasswordToggle('toggleNewPassword', 'newPassword');
    setupPasswordToggle('toggleConfirmPassword', 'confirmPassword');

    // Form submission
    $('#resetPasswordForm').on('submit', function(e) {
        e.preventDefault();

        const newPassword = $('#newPassword').val();
        const confirmPassword = $('#confirmPassword').val();
        const token = $('#token').val();
        const email = $('#email').val();

        if (newPassword !== confirmPassword) {
            showModal('error', 'Password Mismatch', 'Passwords do not match');
            return;
        }

        // Show loading state
        const submitBtn = $(this).find('button[type="submit"]');
        setLoadingState(submitBtn, true);

        $.ajax({
            url: '/forgotPassword/reset-password',
            type: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            data: JSON.stringify({
                newPassword: newPassword,
                confirmPassword: confirmPassword
            }),
            contentType: 'application/json',
            success: function(response) {
                if (response?.status === "success") {
                    showModal('success', 'Success', response.message);
                    setTimeout(() => {
                        window.location.href = '/api/auth/login';
                    }, 1500);
                } else {
                    showModal('error', 'Error', response?.message || 'Failed to reset password');
                }
            },
            error: function(xhr) {
                let errorMessage = 'Failed to reset password. Please try again.';
                try {
                    const errorResponse = xhr.responseJSON || JSON.parse(xhr.responseText);
                    errorMessage = errorResponse.message || errorResponse.error || errorMessage;
                } catch (e) {
                    errorMessage = xhr.responseText || errorMessage;
                }
                showModal('error', 'Error', errorMessage);
            },
            complete: function() {
                setLoadingState(submitBtn, false);
            }
        });
    });

    // Helper functions
    function setLoadingState(element, isLoading) {
        const text = element.text().replace(/^[^a-zA-Z]+/, '');
        element.prop('disabled', isLoading);
        element.html(isLoading
            ? `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ${text}`
            : text);
    }

    function showModal(type, title, message) {
        const modal = type === 'success' ? successModal : errorModal;
        const titleEl = type === 'success' ? '#successModalLabel' : '#errorModalLabel';
        const messageEl = type === 'success' ? '#successModal .modal-body' : '#errorMessage';

        $(titleEl).text(title);
        $(messageEl).html(type === 'success'
            ? `<i class="fas fa-check-circle me-2"></i> ${message}`
            : message);

        modal.show();
    }
});