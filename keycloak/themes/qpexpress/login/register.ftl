<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("registerTitle")}
    <#elseif section = "info">
        <a href="${url.loginUrl}">${msg("backToLogin")}</a>
    <#elseif section = "form">
        <form id="kc-register-form" class="${properties.kcFormClass!}" action="${url.registrationAction}" method="post">
            <div class="${properties.kcRegisterFormClass}">
                <div class="${properties.kcFormColumnClass}">
                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('firstName',properties.kcFormGroupErrorClass!)}">
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="firstName" class="${properties.kcInputClass!}" name="firstName"
                                   placeholder="${msg("firstName")}" value="${(register.formData.firstName!'')}"/>
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('lastName',properties.kcFormGroupErrorClass!)}">
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="lastName" class="${properties.kcInputClass!}" name="lastName"
                                   placeholder="${msg("lastName")}" value="${(register.formData.lastName!'')}"/>
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('patronymic',properties.kcFormGroupErrorClass!)}">
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="user.attributes.patronymic" class="${properties.kcInputClass!}"
                                   name="user.attributes.patronymic" placeholder="${msg("patronymic")}"
                                   value="${(register.formData['user.attributes.patronymic']!'')}"/>
                        </div>
                    </div>
                </div>
                <div class="${properties.kcFormColumnClass}">
                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('email',properties.kcFormGroupErrorClass!)}">
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="email" class="${properties.kcInputClass!}" name="email"
                                   value="${(register.formData.email!'')}" placeholder="${msg("email")}"
                                   autocomplete="email"/>
                        </div>
                    </div>

                    <#if !realm.registrationEmailAsUsername>
                        <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('username',properties.kcFormGroupErrorClass!)}">
                            <div class="${properties.kcLabelWrapperClass!}">
                                <label for="username" class="${properties.kcLabelClass!}">${msg("username")}</label>
                            </div>
                            <div class="${properties.kcInputWrapperClass!}">
                                <input type="text" id="username" class="${properties.kcInputClass!}" name="username"
                                       value="${(register.formData.username!'')}" autocomplete="username"/>
                            </div>
                        </div>
                    </#if>

                    <#if passwordRequired>
                        <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password',properties.kcFormGroupErrorClass!)}">
                            <div class="${properties.kcInputWrapperClass!}">
                                <input type="password" id="password" class="${properties.kcInputClass!}" name="password"
                                       placeholder="${msg("password")}" autocomplete="new-password"/>
                            </div>
                        </div>

                        <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password-confirm',properties.kcFormGroupErrorClass!)}">
                            <div class="${properties.kcInputWrapperClass!}">
                                <input type="password" id="password-confirm" class="${properties.kcInputClass!}"
                                       placeholder="${msg("passwordConfirm")}" name="password-confirm"/>
                            </div>
                        </div>
                    </#if>
                </div>
            </div>

            <#if recaptchaRequired??>
                <div class="form-group">
                    <div class="${properties.kcInputWrapperClass!}">
                        <div class="g-recaptcha" data-size="compact" data-sitekey="${recaptchaSiteKey}"></div>
                    </div>
                </div>
            </#if>
            <div class="${properties.kcTermsAndConditionsClass}">
                <input style="margin-bottom: 8px;" type="checkbox" id="exl-agree-tos" name="exl-agree-tos"
                       required/> ${msg("iAgree")} <a href="https://qpexpress.kz/termsofservice"
                                                      rel="noopener"
                                                      target="_blank">${msg("termsAndPolicies")}</a>.
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                           type="submit" value="${msg("doRegister")}"/>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>