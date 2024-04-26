package com.egi.book.email

enum class EmailTemplateName(val templateName: String) {
    ACTIVATE_ACCOUNT("activate_account"),
    ;

    override fun toString(): String {
        return templateName
    }
}
