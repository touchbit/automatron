/*
 * Copyright (c) 2022 Shaburov Oleg
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.touchbit.qa.automatron.resource.mapping;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
@RequestMapping(method = POST)
public @interface PostRequest {

    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path();

    @AliasFor(annotation = RequestMapping.class, attribute = "consumes")
    String[] requestMediaType() default MediaType.APPLICATION_JSON_VALUE;

    @AliasFor(annotation = RequestMapping.class, attribute = "produces")
    String[] responseMediaType() default MediaType.APPLICATION_JSON_VALUE;

}
