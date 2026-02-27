package com.github.flombois.models;

/**
 * Represents version information about a service or API.
 * <p>
 * This class holds the name and version details of a service, typically used to
 * represent the API version information returned by the Nager.Date API.
 * </p>
 *
 * @since 1.0
 */
public class VersionInfo {

    private String name;
    private String version;

    /**
     * Returns the name of the service or API.
     *
     * @return the service name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the service or API.
     *
     * @param name the service name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the version string of the service or API.
     *
     * @return the version string
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version string of the service or API.
     *
     * @param version the version string
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
