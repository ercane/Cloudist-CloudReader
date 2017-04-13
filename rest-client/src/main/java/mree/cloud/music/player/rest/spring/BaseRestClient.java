package mree.cloud.music.player.rest.spring;/*

 *
 * Project: mcys-rest-client
 * Date Created: 16 Nis 2015
 * Created By: ykurt
 */

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import mree.cloud.music.player.rest.utils.TokenHeaderClientHttpRequestInterceptor;

/**
 * @author ykurt
 */
public abstract class BaseRestClient {

    private static HttpComponentsClientHttpRequestFactory factory;
    protected String SERVICE_URI;
    protected InputStream cert;
    protected String xTokenHeader;
    private List<AbstractHttpMessageConverter> converterList;
    private String xToken;
    private List<ClientHttpRequestInterceptor> interceptorList;

    public BaseRestClient(String SERVICE_URI, String xTokenHeader, String xToken,
                          AbstractHttpMessageConverter converter) {
        this.SERVICE_URI = SERVICE_URI;
        this.xToken = xToken;
        this.xTokenHeader = xTokenHeader;
        converterList = new ArrayList<>();
        converterList.add(converter);
    }

    public String getServiceUri() {
        return SERVICE_URI;
    }

    public InputStream getCert() {
        return cert;
    }

    public void setCert(InputStream cert) {
        this.cert = cert;
    }

    public String getxToken() {
        return xToken;
    }

    public void setxToken(String xToken) {
        this.xToken = xToken;
    }

    public String getxTokenHeader() {
        return xTokenHeader;
    }

    public void setxTokenHeader(String xTokenHeader) {
        this.xTokenHeader = xTokenHeader;
    }

    public void addConverter(AbstractHttpMessageConverter converter) {
        if (converterList == null) {
            converterList = new ArrayList<>();
        }
        converterList.add(converter);
    }

    public ClientHttpRequestFactory clientHttpRequestFactory() {
        if (factory == null) {
            factory = new HttpComponentsClientHttpRequestFactory();
            factory.setReadTimeout(120000);
            factory.setConnectTimeout(30000);
        }
        return factory;
    }

    public void setServiceURI(String serviceURI) {
        SERVICE_URI = serviceURI;
    }

    protected String getFullUri(String uriPart) {
        return SERVICE_URI + uriPart;
    }

    protected <T> T get(String uriPart, Class<T> t, Map<String, ?> parameters) {
        T result = restTemplate().getForObject(SERVICE_URI + uriPart, t, parameters);
        return result;
    }

    protected <T> List<T> getForList(String uriPart, Class<T[]> t, Map<String, ?> parameters) {
        T[] result = restTemplate().getForObject(SERVICE_URI + uriPart, t, parameters);
        return Arrays.asList(result);
    }

    protected <T> ResponseEntity<T> getForEntity(String uriPart, Class<T> t) {

        RestTemplate restTemplate = restTemplate();
        ResponseEntity<T> result = restTemplate.getForEntity(SERVICE_URI + uriPart, t,
                Collections.emptyMap());
        return result;
    }

    protected <T> ResponseEntity<T> getForEntity(String uriPart, Class<T> t, Map<String, ?>
            parameters) {

        RestTemplate restTemplate = restTemplate();
        ResponseEntity<T> result = restTemplate.getForEntity(SERVICE_URI + uriPart, t, parameters);
        return result;
    }

    protected <T> T post(String uriPart, Object t) {
        T result = (T) restTemplate().postForObject(SERVICE_URI + uriPart, t, t.getClass());
        return result;
    }

    protected <T> T post(String uriPart, Object o, Class<T> t) {
        T result = restTemplate().postForObject(SERVICE_URI + uriPart, o, t);
        return result;
    }

    protected <T> T post(String uriPart, Object o, Class<T> t, Map<String, ?> parameters) {
        T result = restTemplate().postForObject(SERVICE_URI + uriPart, o, t, parameters);
        return result;
    }

    protected <T> ResponseEntity<T> postForEntity(String uriPart, Object o, Class<T> t,
                                                  Map<String, ?> parameters) {
        ResponseEntity<T> result = restTemplate().postForEntity(SERVICE_URI + uriPart, o, t,
                parameters);
        return result;
    }

    protected <T> ResponseEntity<T> postForEntity(String uriPart, Object o, Class<T> t,
                                                  ClientHttpRequestInterceptor interceptor) {

        RestTemplate restTemplate = restTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        if (interceptors == null) {
            interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        } else {
            interceptors.clear();
        }

        if (xTokenHeader != null && !xTokenHeader.isEmpty()) {
            interceptorList.add(new TokenHeaderClientHttpRequestInterceptor(xTokenHeader, xToken));
        }

        interceptors.add(interceptor);
        restTemplate.setInterceptors(interceptors);

        ResponseEntity<T> result = restTemplate.postForEntity(SERVICE_URI + uriPart, o, t,
                Collections.emptyMap());
        return result;
    }

    protected <T> ResponseEntity<T> postForEntity(String uriPart, Object o, Class<T> t) {
        return restTemplate().postForEntity(SERVICE_URI + uriPart, o, t, Collections.emptyMap());

    }

    protected <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
                            ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables) {
        return restTemplate().execute(url, method, requestCallback, responseExtractor,
                urlVariables);

    }

    protected <T> ResponseEntity<T> exchangeForEntity(String uriPart, HttpMethod method,
                                                      HttpEntity<String> entity, Class<T> t,
                                                      Map<String, ?> urlVariables) {
        return restTemplate().exchange(SERVICE_URI + uriPart, method, entity, t, urlVariables);

    }

    protected <T> ResponseEntity<T> exchangeForEntity(String uriPart, HttpMethod method,
                                                      HttpEntity<String> entity, Class<T> t) {
        return restTemplate().exchange(SERVICE_URI + uriPart, method, entity, t, Collections
                .EMPTY_MAP);

    }

    protected <T> T executeLarge(String url, HttpMethod method, RequestCallback requestCallback,
                                 ResponseExtractor<T> responseExtractor, Map<String, ?>
                                         urlVariables) {
        RestTemplate rt = restTemplate();
        return rt.execute(url, method, requestCallback, responseExtractor, urlVariables);
    }

    protected <T> List<T> postForList(String uriPart, Object o, Class<T[]> t, Map<String, ?>
            parameters) {
        T[] result = restTemplate().postForObject(SERVICE_URI + uriPart, o, t, parameters);
        return Arrays.asList(result);
    }

    protected void delete(String uriPart, Map<String, ?> parameters) {
        restTemplate().delete(SERVICE_URI + uriPart, parameters);
    }

    protected void delete(String uriPart, Object... parameters) {
        restTemplate().delete(SERVICE_URI + uriPart, parameters);
    }

    protected void put(String uriPart, Object... parameters) {
        restTemplate().put(SERVICE_URI + uriPart, parameters);
    }

    protected <T> T put(String uriPart, Object o, Class<T> t, Map<String, ?> parameters) {
        T result = restTemplate().postForObject(SERVICE_URI + uriPart, o, t, parameters);
        return result;
    }

    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

/*        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());*/
        //restTemplate.getMessageConverters().add(GsonConverter.getGsonConverterInstance());
        for (AbstractHttpMessageConverter ahmc : converterList) {
            restTemplate.getMessageConverters().add(ahmc);
        }


        if (interceptorList == null) {
            interceptorList = new ArrayList<ClientHttpRequestInterceptor>();
        } else {
            interceptorList.clear();
        }
        if (xTokenHeader != null && !xTokenHeader.isEmpty()) {
            interceptorList.add(new TokenHeaderClientHttpRequestInterceptor(xTokenHeader, xToken));
        }
        /*

        if (mdmId != null && !mdmId.isEmpty()) {
            interceptorList.add(new MdmIdHeaderClientHttpRequestInterceptor(mdmId));
        }*/
        restTemplate.setInterceptors(interceptorList);

        return restTemplate;
    }


}
