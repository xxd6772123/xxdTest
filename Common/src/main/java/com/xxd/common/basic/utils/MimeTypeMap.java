package com.xxd.common.basic.utils;

import java.util.regex.Pattern;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class MimeTypeMap {


    private static final MimeTypeMap sMimeTypeMap= new MimeTypeMap();

    private MimeTypeMap(){}

    /**
     * Returns the file extension or an empty string iff there is no
     * extension. This method is a convenience method for obtaining the
     * extension of a url and has undefined results for other Strings.
     * @param url The URL to the file
     * @return The file extension of the given url.
     */
    public static String getFileExtensionFromUrl(String url){
        if(url != null && url.length() > 0){
            int fragment= url.lastIndexOf('#');
            if(fragment > 0){
                url= url.substring(0, fragment);
            }

            int query= url.lastIndexOf('?');
            if(query > 0){
                url= url.substring(0, query);
            }

            int filenamePos= url.lastIndexOf('/');
            String filename= 0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if(!filename.isEmpty() &&
                    Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)){
                int dotPos= filename.lastIndexOf('.');
                if(0 <= dotPos){
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }

    /**
     * Return true if the given MIME type has an entry in the map.
     * @param mimeType A MIME type (i.e. text/plain)
     * @return True iff there is a mimeType entry in the map.
     */
    public boolean hasMimeType(String mimeType){
        return MimeUtils.hasMimeType(mimeType);
    }

    /**
     * Return the MIME type for the given extension.
     * @param extension A file extension without the leading '.'
     * @return The MIME type for the given extension or null iff there is none.
     */
    public String getMimeTypeFromExtension(String extension){
        return MimeUtils.guessMimeTypeFromExtension(extension);
    }

    /**
     * Return true if the given extension has a registered MIME type.
     * @param extension A file extension without the leading '.'
     * @return True iff there is an extension entry in the map.
     */
    public boolean hasExtension(String extension){
        return MimeUtils.hasExtension(extension);
    }

    /**
     * Return the registered extension for the given MIME type. Note that some
     * MIME types map to multiple extensions. This call will return the most
     * common extension for the given MIME type.
     * @param mimeType A MIME type (i.e. text/plain)
     * @return The extension for the given MIME type or null iff there is none.
     */
    public String getExtensionFromMimeType(String mimeType){
        return MimeUtils.guessExtensionFromMimeType(mimeType);
    }

    /**
     * Get the singleton instance of MimeTypeMap.
     * @return The singleton instance of the MIME-type map.
     */
    public static MimeTypeMap getSingleton(){
        return sMimeTypeMap;
    }
}
