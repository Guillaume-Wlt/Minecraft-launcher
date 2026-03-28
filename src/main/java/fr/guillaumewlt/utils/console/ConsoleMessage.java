package fr.guillaumewlt.utils.console;

import fr.guillaumewlt.model.assets.AssetInfos;
import fr.guillaumewlt.model.assets.AssetsIndex;
import fr.guillaumewlt.processing.steps.Processes;
import lombok.Getter;

public enum ConsoleMessage {

    /**
     * Create Message for {@link fr.guillaumewlt.workflow.WorkflowRunner} <br>
     * - Change Step Message -> Message to print the mainTask of the current Step
     *   | %s : the mainTask label of the current {@link fr.guillaumewlt.workflow.ProgramStep} (e.g. {@code "=== Downloading manifest..."}) <br>
     * - Unknown Step error -> Message for when the current step is unknown
     *   | %s : the unknown {@link fr.guillaumewlt.workflow.ProgramStep} value <br>
     * - Ending message -> Message for when the program stops <br>
     */
    WORKFLOW_RUNNER_CHANGE_STEP_MESSAGE(ConsolePrefix.INFO.getLabel() + "%s"),
    WORKFLOW_RUNNER_UNKNOWN_STEP_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Unknown step: %s"),
    WORKFLOW_RUNNER_ENDING_MESSAGE(ConsolePrefix.INFO.getLabel() + "Workflow Ending... >> Starting Game"),

    /**
     * Create Message for {@link fr.guillaumewlt.processing.CheckFoldersExistence}<br>
     * - Created -> Message for when the folder is correctly created
     *   | %s : folder name | %s : absolute path where the folder was created <br>
     * - Exist -> Message for when the folder already exist and is not created
     *   | %s : folder name | %s : absolute path where the folder already exists <br>
     * - Error -> Message for when the folder cannot be created
     *   | %s : folder name | %s : absolute path where the folder creation failed <br>
     *
     */
    CHECK_FOLDER_EXISTENCE_CREATED(ConsolePrefix.INFO.getLabel() + "Folder \"%s\" created in >> %s"),
    CHECK_FOLDER_EXISTENCE_EXIST(ConsolePrefix.INFO.getLabel() + "Folder \"%s\" already exists in >> %s"),
    CHECK_FOLDER_EXISTENCE_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Failed to create folder >> \"%s\", in >> %s"),

    /**
     * Create Message for {@link Processes} <br>
     * - Fatal Error -> Message for when there is a processing error
     *   | %s : the error reason message <br>
     *
     */
    PROCESSES_FATAL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Failed to process >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.utils.DirectoryPathUtils} <br>
     * - Invalid JAR path error -> Message for when the JAR location URI is invalid
     *   | %s : the URI syntax error message <br>
     * - Selected Version directory : Path NULL -> Message for when the Path of the selected version directory ([...]/launcher/versions/&lt;selected_version&gt;) is NULL <br>
     *
     */
    DIRECTORYPATH_UTILS_LAUNCHER_DIR_INVALID_PATH_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Invalid JAR path >> %s"),
    DIRECTORYPATH_UTILS_SELECTED_VERSION_DIR_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Selected Version directory is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.utils.LauncherUtils} <br>
     * - Manifest URL : URL NULL -> Message for when the Manifest URL is NULL <br>
     * - Manifest : Name NULL -> Message for when the Manifest name is NULL <br>
     * - Client Jar : Name NULL -> Message for when the Client Jar name is NULL <br>
     *
     */
    LAUNCHER_UTILS_MANIFEST_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Manifest URL is null"),
    LAUNCHER_UTILS_MANIFEST_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Manifest name is null"),
    LAUNCHER_UTILS_CLIENT_JAR_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Client jar name is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.utils.FilePathUtils} <br>
     * - Manifest : Path NULL -> Message for when the Manifest file Path is NULL <br>
     * - Selected version JSON File : Path NULL -> Message for when the Selected version JSON file Path is NULL <br>
     *
     */
    FILEPATH_UTILS_MANIFEST_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Manifest path is null"),
    FILEPATH_UTILS_SELECTED_VERSION_JSON_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Selected version JSON file path is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.processing.DownloadProgress} <br>
     * - Update message -> Message to display in the console everytime the download progress
     *   | %s : the filename being downloaded | %s : the current download percentage (0-100) <br>
     *
     */
    DOWNLOAD_PROGRESS_UPDATE_MESSAGE(ConsolePrefix.INFO.getLabel() + "%s >> %s%%"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.directory.LauncherDirs} <br>
     * - Launcher Dir : NULL Err -> Message for when the Launcher directory infos are NULL <br>
     * - Bin Dir : NULL Err -> Message for when the Bin directory infos are NULL <br>
     * - Versions Dir : NULL Err -> Message for when the Versions directory infos are NULL <br>
     * - Libraries Dir : NULL Err -> Message for when the Libraries directory infos are NULL <br>
     * - Assets Dir : NULL Err -> Message for when the Assets directory infos are NULL <br>
     * - Assets Indexes Dir : NULL Err -> Message for when the Assets Indexes directory infos are NULL <br>
     * - Assets Objects Dir : NULL Err -> Message for when the Assets Objects directory infos are NULL <br>
     */
    LAUNCHERDIRS_LAUNCHER_DIR_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Launcher directory infos are null"),
    LAUNCHERDIRS_BIN_DIR_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Bin directory infos are null"),
    LAUNCHERDIRS_VERSIONS_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Versions directory infos are null"),
    LAUNCHERDIRS_LIBRARIES_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Libraries directory infos are null"),
    LAUNCHERDIRS_ASSETS_DIR_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets directory infos are null"),
    LAUNCHERDIRS_ASSETS_INDEXES_DIR_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets Indexes directory infos are null"),
    LAUNCHERDIRS_ASSETS_OBJECTS_DIR_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets Objects directory infos are null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.directory.LauncherDir} <br>
     * - Name : NULL Error -> Message for when the name of the directory is NULL <br>
     * - Path : NULL Error -> Message for when the path of the directory is NULL <br>
     *
     */
    LAUNCHERDIR_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "directory name is null"),
    LAUNCHERDIR_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "directory path is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.SelectedVersion} <br>
     * - Selected Version : Record NULL -> Message for when the record is NULL <br>
     * - Selected version : Name NULL -> Message for when the selected version name is NULL <br>
     * - Selected Version URL : URL NULL -> Message for when the Selected version URL is NULL <br>
     */
    SELECTEDVERSION_RECORD_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Selected version record is null"),
    SELECTEDVERSION_RECORD_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Version name is null"),
    SELECTEDVERSION_RECORD_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Selected version URL is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.VersionRawData} <br>
     * - Version Raw Data : JSON Object NULL -> Message for when the Client JSON Object is NULL <br>
     */
    VERSIONRAWDATA_RECORD_CLIENT_JSON_OBJECT_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Client jar infos JSON object is null"),
    VERSIONRAWDATA_RECORD_LIBRARIES_JSON_ARRAY_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Libraries JSON Array is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.ClientJarInfos} <br>
     * - Client Hash : Hash NULL -> Message for when the Selected Client Hash is NULL <br>
     * - Client Size : Size NULL -> Message for when the Selected Client Size is NULL <br>
     * - Client URL : URL NULL -> Message for when the Selected Client URL is NULL <br>
     *
     */
    CLIENTJARINFOS_RECORD_SELECTED_CLIENT_HASH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Selected client jar hash is null"),
    CLIENTJARINFOS_RECORD_SELECTED_CLIENT_SIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Selected client jar size is null"),
    CLIENTJARINFOS_RECORD_SELECTED_CLIENT_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Selected client URL is null"),

    /**
     * Create Message for {@link AssetsIndex} <br>
     * - Asset Index : Id NULL -> Message for when the Assets index ID is null <br>
     * - Asset Index : Hash NULL -> Message for when the Assets index Hash is null <br>
     * - Asset Index : Size NULL -> Message for when the Assets index Size is null <br>
     * - Asset Index : TotalSize NULL -> Message for when the Assets index TotalSize is null <br>
     * - Asset Index : URL NULL -> Message for when the Assets index URL is null <br>
     */
    ASSETSINDEX_RECORD_ID_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets Id is null"),
    ASSETSINDEX_RECORD_HASH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets hash is null"),
    ASSETSINDEX_RECORD_SIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets size is null"),
    ASSETSINDEX_RECORD_TOTALSIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets total size is null"),
    ASSETSINDEX_RECORD_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets URL is null"),

    /**
     * Create Message for {@link AssetInfos} <br>
     * - Hash : NULL Err -> Message for when the hash value of the asset is NULL
     * - Size: NULL Err -> Message for when the size value of the asset is NULL
     *
     */
    ASSETINFOS_RECORD_HASH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Asset hash is null"),
    ASSETINFOS_RECORD_SIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Asset size is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.model.LibraryInfos} <br>
     * - Library : Name NULL -> Message for when the name of the library is NULL <br>
     * - Library : Hash NULL -> Message for when the Hash of the library is NULL <br>
     * - Library : Path NULL -> Message for when the Path of the library is NULL <br>
     * - Library : Size NULL -> Message for when the Size of the library is NULL <br>
     * - Library : URL NULL -> Message for when the URL of the library is NULL <br>
     */
    LIBRARYINFOS_RECORD_NAME_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Library's name is null"),
    LIBRARYINFOS_RECORD_HASH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Library's hash is null"),
    LIBRARYINFOS_RECORD_PATH_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Library's path is null"),
    LIBRARYINFOS_RECORD_SIZE_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Library's size is null"),
    LIBRARYINFOS_RECORD_URL_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Library's URL is null"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.ManifestDownload} <br>
     * - Already up-to-date -> Message for when the manifest is already download and up-to-date <br>
     * - Successful -> Message for when the manifest is successfully downloaded <br>
     * - Error -> Message for when an error happen while downloading the manifest
     *   | %s : the exception error message <br>
     *
     */
    MANIFEST_DOWNLOAD_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getLabel() + "Manifest already up to date, skipping download"),
    MANIFEST_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getLabel() + "Manifest download >> Successful"),
    MANIFEST_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Error downloading manifest: %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.ManifestParser} <br>
     * - Input Message -> Message for the scanner to input the wanted version <br>
     * - Input Version : Version Not Found -> Message for when the version is not found in the Manifest
     *   | %s : the version id entered by the user that was not found (e.g. {@code "1.20.1"}) <br>
     * - URL Set Message -> Message to notice the console that the version URL has been set
     *   | %s : the URL of the selected version JSON file <br>
     *
     */
    MANIFEST_PARSER_SCANNER_INPUT_MESSAGE(ConsolePrefix.INPUT.getLabel() + "Select version to download : "),
    MANIFEST_PARSER_SCANNER_INPUT_VERSION_NOT_FOUND_ERR(ConsolePrefix.ERROR.getLabel() + "Version : %s not found"),
    MANIFEST_PARSER_URL_SET_MESSAGE(ConsolePrefix.INFO.getLabel() + "Version URL set to >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.VersionJSONDownload} <br>
     * - Already up-to-date -> Message for when the Selected Version JSON is already download and up-to-date <br>
     * - Successful -> Message for when the Selected Version JSON is successfully downloaded <br>
     * - Error -> Message for when an error happen while downloading the Selected Version JSON
     *   | %s : the exception error message <br>
     *
     */
    VERSION_JSON_DOWNLOAD_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getLabel() + "Version JSON already up to date, skipping download"),
    VERSION_JSON_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getLabel() + "Version JSON download >> Successful"),
    VERSION_JSON_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Error Downloading Version JSON : %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.VersionJSONParser} <br>
     * - Client Jar Infos Message -> Message to give notice to the console that the Client Jar Infos are well parse <br>
     * - Libraries Infos Message -> Message to give notice to the console that the Libraries Infos are well parse <br>
     * - Assets Infos Message -> Message to give notice to the console that the Assets Infos are well parse <br>
     * - Parsing Error -> Message for when there is an error while parsing the data
     *   | %s : the exception error message <br>
     *
     */
    VERSION_JSON_PARSER_CLIENT_JAR_INFOS_MESSAGE(ConsolePrefix.INFO.getLabel() + ">> Client Jar Infos: OK"),
    VERSION_JSON_PARSER_LIBRARIES_INFOS_MESSAGE(ConsolePrefix.INFO.getLabel() + ">> Libraries Infos : OK"),
    VERSION_JSON_PARSER_ASSETS_INFOS_MESSAGE(ConsolePrefix.INFO.getLabel() + ">> Assets Infos : OK"),
    VERSION_JSON_PARSER_PARSING_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Error while parsing the Selected Version JSON Informations : %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.ClientJarInfosParser} <br>
     * - URL Message -> Message to show the client URL to the console
     *   | %s : the download URL of the client JAR <br>
     * - HASH Message -> Message to show the client HASH to the console
     *   | %s : the SHA-1 hash of the client JAR <br>
     * - SIZE Message -> Message to show the client SIZE to the console
     *   | %s : the size of the client JAR in Mo <br>
     *
     */
    CLIENT_JAR_INFOS_PARSER_URL_MESSAGE(ConsolePrefix.INFO.getLabel() + "Download URL >> %s"),
    CLIENT_JAR_INFOS_PARSER_HASH_MESSAGE(ConsolePrefix.INFO.getLabel() + "Client Hash (Sha1) >> %s"),
    CLIENT_JAR_INFOS_PARSER_SIZE_MESSAGE(ConsolePrefix.INFO.getLabel() + "Client Size >> %sMo"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.ClientJarDownload} <br>
     * - Local Client Hash Message -> Message to notice the console of the current local client hash
     *   | %s : the SHA-1 hash of the local client JAR file <br>
     * - Client Already up-to-date -> Message to show the client Jar is already downloaded and up-to-date <br>
     * - Client Jar Corrupted -> Message to show when the local client file is corrupted <br>
     * - Download Successful -> Message to show when the download of the client Jar is successful
     *   | %s : the version name of the downloaded client JAR (e.g. {@code "1.20.1"}) <br>
     * - Download Error -> Message to show when the download of the client Jar has encounter an error
     *   | %s : the exception error message <br>
     *
     */
    CLIENT_JAR_DOWNLOAD_LOCAL_CLIENT_HASH_MESSAGE(ConsolePrefix.INFO.getLabel() + "Local Client Jar >> %s"),
    CLIENT_JAR_DOWNLOAD_CLIENT_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getLabel() + "Client JAR already exists and is correct, skipping download"),
    CLIENT_JAR_DOWNLOAD_CLIENT_JAR_CORRUPTED_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Client Jar is corrupted, file deleted"),
    CLIENT_JAR_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getLabel() + "Client JAR >> %s.jar has been successfully downloaded"),
    CLIENT_JAR_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Error downloading client jar >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.LibrariesDownload} <br>
     * - Libraries : list NULL -> Message for when the list ({@code List<LibraryInfos>}) contains no library <br>
     * - Local Library : File Hash message -> Message that display the hash of the local library file
     *   | %s : the SHA-1 hash of the local library JAR file <br>
     * - Already up-to-date message -> Message for when the local library file is up-to-date and don't need to be re-download
     *   | %s : the name of the library already up-to-date <br>
     * - Corrupt Error -> Message for when the downloaded file is corrupted
     *   | %s : the name of the corrupted library <br>
     * - Successful -> Message for when the download is successful
     *   | %s : the name of the successfully downloaded library <br>
     * - Error -> Message for when a problem happen while the file were getting download
     *   | %s : the name of the library | %s : the exception error message <br>
     *
     */
    LIBRARIESDOWNLOAD_LIBRARIES_LIST_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "No libraries found"),
    LIBRARIESDOWNLOAD_LOCAL_LIBRARY_FILE_HASH_MESSAGE(ConsolePrefix.INFO.getLabel() + "Local Library File Hash >> %s"),
    LIBRARIESDOWNLOAD_ALREAY_UP_TO_DATE(ConsolePrefix.INFO.getLabel() + "%s already up-to-date, skipping download"),
    LIBRARIESDOWNLOAD_CORRUPT_ERR(ConsolePrefix.FATAL_ERROR.getLabel() +  "Error downloading >> %s, the file is corrupted"),
    LIBRARIESDOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getLabel() + "%s has been successfully downloaded"),
    LIBRARIESDOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Error downloading library :  %s >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.AssetsIndexParser} <br>
     * - Id Message -> Message to display the current Assets Id
     *   | %s : the asset index id (e.g. {@code "1.20"}) <br>
     * - Hash Message -> Message to display the current Assets Hash
     *   | %s : the SHA-1 hash of the asset index JSON file <br>
     * - Size Message -> Message to display the current Assets Size
     *   | %s : the size of the asset index JSON file in Mo <br>
     * - TotalSize Message -> Message to display the current Assets TotalSize
     *   | %s : the combined size of all asset files in Mo <br>
     * - URL Message -> Message to display the current Assets URL
     *   | %s : the download URL of the asset index JSON file <br>
     */
    ASSETSINDEX_PARSER_ID_MESSAGE(ConsolePrefix.INFO.getLabel() + "Assets Id >> %s"),
    ASSETSINDEX_PARSER_HASH_MESSAGE(ConsolePrefix.INFO.getLabel() + "Assets Hash >> %s"),
    ASSETSINDEX_PARSER_SIZE_MESSAGE(ConsolePrefix.INFO.getLabel() + "Assets Size >> %sMo"),
    ASSETSINDEX_PARSER_TOTALSIZE_MESSAGE(ConsolePrefix.INFO.getLabel() + "Assets Total Size >> %sMo"),
    ASSETSINDEX_PARSER_URL_MESSAGE(ConsolePrefix.INFO.getLabel() + "Assets URL >> %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.AssetsIndexDownload} <br>
     * - Local File : Hash Message -> Message to display the local Assets Index file hash
     *   | %s : the SHA-1 hash of the local asset index JSON file <br>
     * - File : Already up-to-date -> Message for when the Assets Index File is already downloaded and up-to-date <br>
     * - File : Corrupted Error -> Message for when the downloaded Assets index file is corrupted after download <br>
     * - File : Download Successful -> Message for when the download is successful <br>
     * - Download Error -> Message for when there was an error trying to download the file
     *   | %s : the exception error message <br>
     */
    ASSETSINDEX_DOWNLOAD_LOCAL_FILE_HASH_MESSAGE(ConsolePrefix.INFO.getLabel() + "Local Assets Index File Hash >> %s"),
    ASSETSINDEX_DOWNLOAD_FILE_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getLabel() + "Assets index already exists and is up-to-date"),
    ASSETSINDEX_DOWNLOAD_FILE_CORRUPTED_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets index is corrupted, file deleted"),
    ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getLabel() + "Assets index has been successfully downloaded"),
    ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Error downloading assets index, error : %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.parser.AssetsInfosParser} <br>
     * - Path Message -> Message to display where the file to parse is located
     *   | %s : the absolute path of the asset index JSON file being parsed <br>
     * - Parsing Error -> Message to display a potential error happening during parsing and the location of the file
     *   | %s : the exception error message <br>
     */
    ASSETSINFOS_PARSER_LOADING_POINT_PATH_MESSAGE(ConsolePrefix.INFO.getLabel() + "Assets Info Parser Load Point Path >> %s"),
    ASSETSINFOS_PARSER_LOADING_POINT_PARSING_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets Info Parser Load Point Parsing error : %s"),

    /**
     * Create Message for {@link fr.guillaumewlt.downloads.AssetsObjectsDownload} <br>
     * - Assets List : NULL or empty -> Message for when the assets list ({@code List<AssetInfos>}) is null or empty <br>
     * - Local Asset File : Hash Message -> Message to display the hash of the local asset file
     *   | %s : the SHA-1 hash of the local asset file <br>
     * - Asset : Already up-to-date -> Message for when the local asset file is already downloaded and up-to-date <br>
     * - Corrupted Error -> Message for when the downloaded asset file is corrupted after download
     *   | %s : the SHA-1 hash of the corrupted asset file <br>
     * - Successful -> Message for when the asset file has been successfully downloaded <br>
     * - Error -> Message for when an error occurred while downloading the asset file
     *   | %s : the exception error message <br>
     */
    ASSETSOBJECTS_DOWNLOAD_ASSETS_LIST_NULL_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Assets List is null or empty"),
    ASSETSOBJECTS_DOWNLOAD_LOCAL_ASSET_FILE_HASH_MESSAGE(ConsolePrefix.INFO.getLabel() + "Local Assets File Hash >> %s"),
    ASSETSOBJECTS_DOWNLOAD_ASSET_ALREADY_UP_TO_DATE(ConsolePrefix.INFO.getLabel() + "local asset file is already downloaded and up-to-date"),
    ASSETSOBJECTS_DOWNLOAD_CORRUPTED_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Downloaded Asset file >> %s is corrupted"),
    ASSETSOBJECTS_DOWNLOAD_SUCCESSFUL(ConsolePrefix.INFO.getLabel() + "Assets file has been successfully downloaded"),
    ASSETSOBJECTS_DOWNLOAD_ERR(ConsolePrefix.FATAL_ERROR.getLabel() + "Error downloading assets file >> %s");

    @Getter
    private String message;

    ConsoleMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
