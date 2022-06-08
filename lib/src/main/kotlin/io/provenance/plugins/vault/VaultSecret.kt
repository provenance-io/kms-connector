package io.provenance.plugins.vault

data class VaultSecret(
    val leaseId: String?,
    val renewable: Boolean?,
    val leaseDuration: Int?,
    val data: SecretData?,
    val auth: SecretAuth?,
    val errors: Array<String>?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VaultSecret

        if (leaseId != other.leaseId) return false
        if (renewable != other.renewable) return false
        if (leaseDuration != other.leaseDuration) return false
        if (data != other.data) return false
        if (auth != other.auth) return false

        return true
    }

    override fun hashCode(): Int {
        var result = leaseId.hashCode()
        result = 31 * result + renewable.hashCode()
        result = 31 * result + leaseDuration!!
        result = 31 * result + data.hashCode()
        result = 31 * result + auth.hashCode()
        return result
    }
}
