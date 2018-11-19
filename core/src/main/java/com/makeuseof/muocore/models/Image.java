/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Image {
    public String mobile; // 500x250px
    public String thumb;  // 300x110px
    public String middle; // 206x122px
    public String post;   // 644x373px
    public String featured; // 297x141px

    public Image() {}
}
