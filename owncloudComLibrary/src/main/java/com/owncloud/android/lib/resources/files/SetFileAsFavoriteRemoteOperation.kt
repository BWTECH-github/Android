/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2025 ownCloud GmbH.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.owncloud.android.lib.resources.files

import com.owncloud.android.lib.common.OwnCloudClient
import com.owncloud.android.lib.common.http.HttpConstants
import com.owncloud.android.lib.common.http.methods.webdav.ProppatchMethod
import com.owncloud.android.lib.common.network.WebdavUtils
import com.owncloud.android.lib.common.operations.RemoteOperation
import com.owncloud.android.lib.common.operations.RemoteOperationResult
import com.owncloud.android.lib.common.operations.RemoteOperationResult.ResultCode
import com.owncloud.android.lib.common.utils.isOneOf
import timber.log.Timber
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Remote operation setting or unsetting the `oc:favorite` WebDAV property of a remote file or
 * folder, keeping the favorite state in sync with the same property used by the web client.
 */
class SetFileAsFavoriteRemoteOperation(
    private val remotePath: String,
    private val favorite: Boolean,
    private val spaceWebDavUrl: String? = null,
) : RemoteOperation<Unit>() {

    override fun run(client: OwnCloudClient): RemoteOperationResult<Unit> {
        return try {
            val proppatchMethod = ProppatchMethod(
                url = URL((spaceWebDavUrl ?: client.userFilesWebDavUri.toString()) + WebdavUtils.encodePath(remotePath)),
                xmlBody = proppatchXmlBody(favorite),
            ).apply {
                setReadTimeout(PROPPATCH_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                setConnectionTimeout(PROPPATCH_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            }

            val status = client.executeHttpMethod(proppatchMethod)

            val result = if (isSuccess(status)) {
                RemoteOperationResult<Unit>(ResultCode.OK)
            } else {
                RemoteOperationResult(proppatchMethod)
            }

            Timber.i("Set favorite=$favorite for $remotePath - HTTP status code: $status")
            client.exhaustResponse(proppatchMethod.getResponseBodyAsStream())
            result
        } catch (exception: Exception) {
            RemoteOperationResult<Unit>(exception).also {
                Timber.e(exception, "Set favorite=$favorite for $remotePath: ${it.logMessage}")
            }
        }
    }

    private fun isSuccess(status: Int) = status.isOneOf(HttpConstants.HTTP_MULTI_STATUS, HttpConstants.HTTP_OK)

    companion object {
        private const val PROPPATCH_READ_TIMEOUT = 10_000L
        private const val PROPPATCH_CONNECTION_TIMEOUT = 5_000L

        private fun proppatchXmlBody(favorite: Boolean): String =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<d:propertyupdate xmlns:d=\"DAV:\" xmlns:oc=\"http://owncloud.org/ns\">" +
                "<d:set><d:prop><oc:favorite>${if (favorite) 1 else 0}</oc:favorite></d:prop></d:set>" +
                "</d:propertyupdate>"
    }
}
