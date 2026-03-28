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
    SELECTEDVERSION_RECORD_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Selected version record is null"),
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
     * Create Message for {@link fr.guillaumewlt.model.AssetsIndex} <br>
     * - Asset Index : Id NULL -> Message for when the Assets index ID is null <br>
     * - Asset Index : Hash NULL -> Message for when the Assets index Hash is null <br>
     * - Asset Index : Size NULL -> Message for when the Assets index Size is null <br>
     * - Asset Index : TotalSize NULL -> Message for when the Assets index TotalSize is null <br>
     * - Asset Index : URL NULL -> Message for when the Assets index URL is null <br>
     */
    ASSETSINDEX_RECORD_ID_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Assets Id is null"),
    ASSETSINDEX_RECORD_HASH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Assets hash is null"),
    ASSETSINDEX_RECORD_SIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Assets size is null"),
    ASSETSINDEX_RECORD_TOTALSIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Assets total size is null"),
    ASSETSINDEX_RECORD_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Assets URL is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.LibraryInfos} <br>
     * - Library : Name NULL -> Message for when the name of the library is NULL <br>
     * - Library : Hash NULL -> Message for when the Hash of the library is NULL <br>
     * - Library : Path NULL -> Message for when the Path of the library is NULL <br>
     * - Library : Size NULL -> Message for when the Size of the library is NULL <br>
     * - Library : URL NULL -> Message for when the URL of the library is NULL <br>
     */
    LIBRARYINFOS_RECORD_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Library's name is null"),
    LIBRARYINFOS_RECORD_HASH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Library's hash is null"),
    LIBRARYINFOS_RECORD_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Library's path is null"),
    LIBRARYINFOS_RECORD_SIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Library's size is null"),
    LIBRARYINFOS_RECORD_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Library's URL is null"),

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
     * - Assets Infos Message -> Message to give notice to the console that the Assets Infos are well parse <br>
     * - Parsing Error -> Message for when there is an error while parsing the data <br>
     *
     */
    VERSION_JSON_PARSER_CLIENT_JAR_INFOS_MESSAGE(ConsolePrefix.INFO.getPrefix() + ">> Client Jar Infos: OK"),
    VERSION_JSON_PARSER_LIBRARIES_INFOS_MESSAGE(ConsolePrefix.INFO.getPrefix() + ">> Libraries Infos : OK"),
    VERSION_JSON_PARSER_ASSETS_INFOS_MESSAGE(ConsolePrefix.INFO.getPrefix() + ">> Assets Infos : OK"),
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
    CLIENT_JAR_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Error downloading client jar >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.LibrariesDownload} <br>
     * - Libraries : list NULL -> Message for when the list (List<LibraryInfos>) contains no library <br>
     * - Local Library : File Hash message -> Message that display the hash of the local library file <br>
     * - Already up-to-date message -> Message for when the local library file is up-to-date and don't need to be re-download <br>
     * - Corrupt Error -> Message for when the downloaded file is corrupted <br>
     * - Successful -> Message for when the download is successful <br>
     * - Error -> Message for when a problem happen while the file were getting download <br>
     *
     */
    LIBRARIESDOWNLOAD_LIBRARIES_LIST_NULL_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "No libraries found"),
    LIBRARIESDOWNLOAD_LOCAL_LIBRARY_FILE_HASH_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Local Library File Hash >> %s"),
    LIBRARIESDOWNLOAD_ALREAY_UP_TO_DATE(ConsolePrefix.INFO.getPrefix() + "%s already up-to-date, skipping download"),
    LIBRARIESDOWNLOAD_CORRUPT_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() +  "Error downloading >> %s, the file is corrupted"),
    LIBRARIESDOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getPrefix() + "%s has been successfully downloaded"),
    LIBRARIESDOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Error downloading library :  %s >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.AssetsIndexParser} <br>
     * - Id Message -> Message to display the current Assets Id <br>
     * - Hash Message -> Message to display the current Assets Hash <br>
     * - Size Message -> Message to display the current Assets Size <br>
     * - TotalSize Message -> Message to display the current Assets TotalSize <br>
     * - URL Message -> Message to display the current Assets URL <br>
     */
    ASSETSINDEX_PARSER_ID_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Assets Id >> %s"),
    ASSETSINDEX_PARSER_HASH_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Assets Hash >> %s"),
    ASSETSINDEX_PARSER_SIZE_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Assets Size >> %sMo"),
    ASSETSINDEX_PARSER_TOTALSIZE_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Assets Total Size >> %sMo"),
    ASSETSINDEX_PARSER_URL_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Assets URL >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.AssetsIndexDownload} <br>
     * - Local File : Hash Message -> Message to display the local Assets Index file <br>
     * - File : Already up-to-date -> Message for when the Assets Index File is already downloaded and up-to-date <br>
     * - File : Corrupted Error -> Message for when the downloaded Assets index file is corrupted after download <br>
     * - File : Download Successful -> Message for when the download is successful <br>
     * - Download Error -> Message for when there was an error trying to download the file <br>
     */
    ASSETSINDEX_DOWNLOAD_LOCAL_FILE_HASH_MESSAGE(ConsolePrefix.INFO.getPrefix() + "Local Assets Index File Hash >> %s"),
    ASSETSINDEX_DOWNLOAD_FILE_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getPrefix() + "Assets index already exists and is up-to-date"),
    ASSETSINDEX_DOWNLOAD_FILE_CORRUPTED_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Assets index is corrupted, file deleted"),
    ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getPrefix() + "Assets index has been successfully downloaded"),
    ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getPrefix() + "Error downloading assets index, error : %s"),;

    @Getter
    private String message;

    ConsoleMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
