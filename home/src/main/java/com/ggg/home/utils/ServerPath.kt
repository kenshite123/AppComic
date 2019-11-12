package com.ggg.home.utils

class ServerPath {
    companion object {
        var baseUri = "http://hronline.upulse.io"
        //        var baseUri = "http://app.aloha-job.com"
        var login = "/cosmo/auth/login"
        var googleUri = "https://maps.googleapis.com"

        const val GEOCODE = "/maps/api/geocode/json"
        const val DIRECTION = "/maps/api/directions/json"

        //        const val GEO_KEY = "AIzaSyB-WNpJuWT7msPD0ziSQkSzMizuRK9ZZb8"
        const val GEO_KEY = "AIzaSyBGdUKnXANtJBeGlbQfoVldVbBc3z72Yxg"
//        fun getMoreInfo():String { return "This is more fun" }

        const val LOGIN = "/api/auth/login"
        const val LOGOUT = "/api/auth/logout"

        private const val CANDIDATE_PREFIX = "/api/employees/"
        private const val EMPLOYER_PREFIX = "/api/employers/"
        private const val CATEGORIES_PREFIX = "/api/categories/"


        const val USER_INFO = "/api/me"

        //candidate
        const val CANDIDATE_NEW_EMPLOYEE = CANDIDATE_PREFIX
        const val CANDIDATE_DETAIL = CANDIDATE_PREFIX.plus("{candidateId}")
        const val CANDIDATE_UPDATE_EMPLOYEE = CANDIDATE_PREFIX.plus("{candidateId}")
        const val CANDIDATE_DELETE_EMPLOYEE = CANDIDATE_PREFIX.plus("{candidateId}")
        const val CANDIDATE_SEARCH_JOB = CANDIDATE_PREFIX.plus("{candidateId}/jobs/search")
        const val CANDIDATE_JOB_DETAIL = CANDIDATE_PREFIX.plus("{candidateId}/jobs/{jobId}")
        const val CANDIDATE_REPORT_JOB = CANDIDATE_PREFIX.plus("{candidateId}/jobs/{jobId}/report")
        const val CANDIDATE_ADD_FAVORITE = CANDIDATE_PREFIX.plus("{candidateId}/jobs/{jobId}/favorite")
        const val CANDIDATE_FAVORITE_JOB_LIST = CANDIDATE_PREFIX.plus("{candidateId}/jobs/favorites")
        const val CANDIDATE_APPLY_JOB = CANDIDATE_PREFIX.plus("{candidateId}/jobs/{jobId}/apply")
        const val CANDIDATE_VERIFY_CODE = CANDIDATE_PREFIX.plus("{candidateId}/activate")
        const val CANDIDATE_GET_APPLIES = CANDIDATE_PREFIX.plus("{candidateId}/jobs/applied")

        //employer
        const val EMPLOYER_NEW_EMPLOYER = EMPLOYER_PREFIX
        const val EMPLOYER_UPDATE_EMPLOYER = EMPLOYER_PREFIX.plus("%s")
        const val EMPLOYER_DELETE_EMPLOYER = EMPLOYER_PREFIX.plus("%s")
        const val EMPLOYER_VERIFY_CODE = EMPLOYER_PREFIX.plus("{employerId}/activate")
        const val EMPLOYER_SEARCH_CANDIDATE = EMPLOYER_PREFIX.plus("{employerId}/employees/search")
        const val EMPLOYER_REPORT_EMPLOYEE = EMPLOYER_PREFIX.plus("{employerId}/employees/{candidateId}/report")
        const val EMPLOYER_CREATE_JOB = EMPLOYER_PREFIX.plus("{employerId}/jobs")
        const val EMPLOYER_UPDATE_JOB = EMPLOYER_PREFIX.plus("{employerId}/jobs/{jobId}")
        const val EMPLOYER_JOB_UPLOAD_BANNER = EMPLOYER_PREFIX.plus("{employerId}/jobs/{jobId}/banners")
        const val EMPLOYER_SEARCH_JOB = EMPLOYER_PREFIX.plus("{employerId}/jobs")
        const val EMPLOYER_SEARCH_CANDIDATE_RELATED = EMPLOYER_PREFIX.plus("{employerId}/employees/related")
        const val EMPLOYER_UPDATE_CANDIDATE_STATUS = EMPLOYER_PREFIX.plus("{employerId}/jobs/{jobId}/employees/{id}")


        //Message
        const val CHAT = "/api/chat"
        const val EMPLOYEE_CHAT = "/api/employees/{value}/chats"
        const val EMPLOYER_CHAT = "/api/employers/{value}/chats"
        const val UPLOAD_CHAT_IMAGE = "/api/chat/media"
        const val EMPLOYER_DETAIL = EMPLOYER_PREFIX.plus("{employerId}")
        const val EMPLOYER_DETAIL_UPDATE = EMPLOYER_PREFIX.plus("{employerId}")

        //categories
        const val CATEGORY_LIST = CATEGORIES_PREFIX


        //common
        const val RESET_PASSWORD = "/api/auth/password"
        const val REACTIVE_USER = "api/users/request_activate"
        const val ACTIVE_USER = "/api/users/activate"
        const val SEARCH_JOB = "/api/jobs/search"

        //notify
        //MARK: - Notify
        const val HANDLE_NOTIFY = "/api/admin/notifications"
        const val EMPLOYEE_NOTIFY = "/api/employees/{value}/notifications"
        const val EMPLOYER_NOTIFY = "/api/employers/{value}/notifications"
        const val IMAGE_NOTIFY = "/api/admin/notifications/media"

        //
        const val UPLOAD_DEGREE = "/api/employees/{employeeId}/degrees"
        const val DELETE_DEGREE = "/api/employees/{employeeId}/degrees/{degreeId}"
        const val UPLOAD_EMPLOYEE_AVATAR = "/api/employees/{employeeId}/avatars"
        const val UPLOAD_EMPLOYER_AVATAR = "/api/employers/{employeeId}/avatars"
        const val UPLOAD_EMPLOYER_BANNER = "/api/employers/{employerId}/banners "

        const val COMPANY_INFO = "/api/company/{employerId}"

        const val AREA = "/api/areas"
        const val YEAR_OF_EXP = "/api/year_of_experiences"
        const val GET_BONUS_TYPE = "/api/bonus_types"
        const val GET_SALARY_TYP = "/api/salary_types"
        const val GET_ALL_STATUS = "/api/employee_statuses"

        const val GET_CANDIDATE_INFO = EMPLOYER_PREFIX.plus("{employerId}/employees/{employeeId}")

        const val DELETE_BANNER = EMPLOYER_PREFIX.plus("/{employerId}/jobs/{jobId}/banners/{bannerId}")

        const val JOBS_FROM_EMPLOYER = CANDIDATE_PREFIX.plus("/{candidateId}/employers/{employerId}/jobs")

        const val FEEDBACK = "/api/feedbacks"
    }
}
