package fr.guillaumewlt.utils.console;

import fr.guillaumewlt.processing.steps.Processes;
import lombok.Getter;

public enum ConsoleMessage {

    /**
     * Create Message for {@link fr.guillaumewlt.workflow.WorkflowRunner} <br>
     * - Change Step Message -> Message to print the mainTask of the current Step <br>
     * - Unknown Step error -> Message for when the current step is unknown <br>
     * - Ending message -> Message for when the program stops <br>
     */
    WORKFLOW_RUNNER_CHANGE_STEP_MESSAGE(ConsolePrefix.INFO.getPrefix() + "%s"),
    WORKFLOW_RUNNER_UNKNOWN_STEP_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Unknown step: %s"),
    WORKFLOW_RUNNER_ENDING_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Ending..."),

    /**
     * Create Message for {@link fr.guillaumewlt.processing.CheckFoldersExistence}<br>
     * - Created -> Message for when the folder is correctly created<br>
     * - Exist -> Message for when the folder already exist and is not created<br>
     * - Error -> Message for when the folder cannot be created<br>
     *
     */
    CHECK_FOLDER_EXISTENCE_CREATED(ConsolePrefix.INFO.getPrefix() + "Folder \"%s\" created in >> %s"),
    CHECK_FOLDER_EXISTENCE_EXIST(ConsolePrefix.INFO.getPrefix() + "Folder \"%s\" already exists in >> %s"),
    CHECK_FOLDER_EXISTENCE_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Failed to create folder >> \"%s\", in >> %s"),

    /**
     * Create Message for {@link Processes} <br>
     * - Fatal Error -> Message for when there is a processing error<br>
     *
     */
    PROCESSES_FATAL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Failed to process %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.utils.DirectoryPathUtils} <br>
     * - Launcher directory : Invalid path -> Message for when the Path of the launcher directory ([...]/launcher/) is invalid <br>
     * - Launcher directory : Path NULL -> Message for when the Path of the launcher directory ([...]/launcher/) is NULL <br>
     * - Versions directory : Path NULL -> Message for when the Path of the versions directory ([...]/launcher/versions/) is NULL <br>
     * - Selected Version directory : Path NULL -> Path NULL -> Message for when the Path of the selected version directory ([...]/launcher/versions/<selected_version>) is NULL <br>
     *
     */
    DIRECTORYPATH_UTILS_LAUNCHER_DIR_INVALID_PATH_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Invalid JAR path >> %s"),
    DIRECTORYPATH_UTILS_LAUNCHER_DIR_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Launcher directory is null"),
    DIRECTORYPATH_UTILS_VERSIONS_DIR_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Versions directory is null"),
    DIRECTORYPATH_UTILS_SELECTED_VERSION_DIR_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Selected Version directory is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.utils.LauncherUtils} <br>
     * - Manifest URL : URL NULL -> Message for when the Manifest URL is NULL <br>
     * - Manifest : Name NULL -> Message for when the Manifest name is NULL <br>
     * - Client Jar : Name NULL -> Message for when the Client Jar name is NULL <br>
     *
     */
    LAUNCHER_UTILS_MANIFEST_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Manifest URL is null"),
    LAUNCHER_UTILS_MANIFEST_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Manifest name is null"),
    LAUNCHER_UTILS_CLIENT_JAR_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Client jar name is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.utils.FilePathUtils} <br>
     * - Manifest : Path NULL -> Message for when the Manifest file Path is NULL <br>
     * - Selected version JSON File : Path NULL -> Message for when the Selected version JSON file Path is NULL <br>
     *
     */
    FILEPATH_UTILS_MANIFEST_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Manifest path is null"),
    FILEPATH_UTILS_SELECTED_VERSION_JSON_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Selected version JSON file path is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.processing.DownloadProgress} <br>
     * - Update message -> Message to display in the console everytime the download progress
     *
     */
    DOWNLOAD_PROGRESS_UPDATE_MESSAGE(ConsolePrefix.INFO.getPrefix() + "%s >> %s%%"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.SelectedVersion} <br>
     * - Selected Version : Record NULL -> Message for when the record is NULL <br>
     * - Selected version : Name NULL -> Message for when the selected version name is NULL <br>
     * - Selected Version URL : URL NULL -> Message for when the Selected version URL is NULL <br>
     */
    SELECTEDVERSION_RECORD_NULL_ERR(ConsolePrefix.INFO.getPrefix() + "Selected version record is null"),
    SELECTEDVERSION_RECORD_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Version name is null"),
    SELECTEDVERSION_RECORD_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Selected version URL is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.VersionRawData}
     * - Version Raw Data : JSON Object NULL -> Message for when the Client JSON Object is NULL <br>
     */
    VERSIONRAWDATA_RECORD_CLIENT_JSON_OBJECT_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Client jar infos JSON object is null"),
    VERSIONRAWDATA_RECORD_LIBRARIES_JSON_ARRAY_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Libraries JSON Array is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.ClientJarInfos} <br>
     * - Client Hash : Hash NULL -> Message for when the Selected Client Hash is NULL <br>
     * - Client Size : Size NULL -> Message for when the Selected Client Size is NULL <br>
     * - Client URL : URL NULL -> Message for when the Selected Client URL is NULL <br>
     *
     */
    CLIENTJARINFOS_RECORD_SELECTED_CLIENT_HASH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Selected client jar hash is null"),
    CLIENTJARINFOS_RECORD_SELECTED_CLIENT_SIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Selected client jar size is null"),
    CLIENTJARINFOS_RECORD_SELECTED_CLIENT_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Selected client URL is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.ManifestDownload} <br>
     * - Already up-to-date -> Message for when the manifest is already download and up-to-date <br>
     * - Successful -> Message for when the manifest is successfully downloaded <br>
     * - Error -> Message for when an error happen while downloading the manifest <br>
     *
     */
    MANIFEST_DOWNLOAD_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getPrefix() + "Manifest already up to date, skipping download"),
    MANIFEST_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getPrefix() + "Manifest download >> Successful"),
    MANIFEST_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Error downloading manifest: %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.ManifestParser} <br>
     * - Input Message -> Message for the scanner to input the wanted version <br>
     * - Input Version : Version Not Found -> Message for when the version is not found in the Manifest <br>
     * - URL Set Message -> Message to notice the console that the version URL has been set <br>
     *
     */
    MANIFEST_PARSER_SCANNER_INPUT_MESSAGE(ConsolePrefix.INPUT.getPrefix() + "Select version to download : "),
    MANIFEST_PARSER_SCANNER_INPUT_VERSION_NOT_FOUND_ERR(ConsolePrefix.ERROR.getPrefix() + "Version : %s not found"),
    MANIFEST_PARSER_URL_SET_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Version URL set to >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.VersionJSONDownload} <br>
     * - Already up-to-date -> Message for when the Selected Version JSON is already download and up-to-date <br>
     * - Successful -> Message for when the Selected Version JSON is successfully downloaded <br>
     * - Error -> Message for when an error happen while downloading the Selected Version JSON <br>
     *
     */
    VERSION_JSON_DOWNLOAD_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getPrefix() + "Version JSON already up to date, skipping download"),
    VERSION_JSON_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getPrefix() + "Version JSON download >> Successful"),
    VERSION_JSON_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Error Downloading Version JSON : %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.VersionJSONParser} <br>
     * - Client Jar Infos Message -> Message to give notice to the console that the Client Jar Infos are well parse <br>
     * - Libraries Infos Message -> Message to give notice to the console that the Libraries Infos are well parse <br>
     * - Parsing Error -> Message for when there is an error while parsing the data <br>
     *
     */
    VERSION_JSON_PARSER_CLIENT_JAR_INFOS_MESSAGE(ConsolePrefix.INFO.getPrefix() + ">> Client Jar Infos: OK"),
    VERSION_JSON_PARSER_LIBRARIES_INFOS_MESSAGE(ConsolePrefix.INFO.getPrefix() + ">> Libraries Infos : OK"),
    VERSION_JSON_PARSER_PARSING_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Error while parsing the Selected Version JSON Informations : %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.ClientJarInfosParser} <br>
     * - URL Message -> Message to show the client URL to the console <br>
     * - HASH Message -> Message to show the client HASH to the console <br>
     * - SIZE Message -> Message to show the client SIZE to the console <br>
     *
     */
    CLIENT_JAR_INFOS_PARSER_URL_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Download URL >> %s"),
    CLIENT_JAR_INFOS_PARSER_HASH_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Client Hash (Sha1) >> %s"),
    CLIENT_JAR_INFOS_PARSER_SIZE_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Client Size >> %sMo"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.ClientJarDownload} <br>
     * - Local Client Hash Message -> Message to notice the console of the current local client hash <br>
     * - Client Already up-to-date -> Message to show the client Jar is already downloaded and up-to-date <br>
     * - Client Jar Corrupted -> Message to show when the local client file is corrupted <br>
     * - Download Successful -> Message to show when the download of the client Jar is successful <br>
     * - Download Error -> Message to show when the download of the client Jar has encounter an error <br>
     *
     */
    CLIENT_JAR_DOWNLOAD_LOCAL_CLIENT_HASH_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Local Client Jar >> %s"),
    CLIENT_JAR_DOWNLOAD_CLIENT_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getPrefix() + "Client JAR already exists and is correct, skipping download"),
    CLIENT_JAR_DOWNLOAD_CLIENT_JAR_CORRUPTED_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Client Jar is corrupted, file deleted"),
    CLIENT_JAR_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getPrefix() + "Client JAR >> %s.jar has been successfully downloaded"),
    CLIENT_JAR_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Error downloading client jar >> %s");

    @Getter
    private String message;

    ConsoleMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
