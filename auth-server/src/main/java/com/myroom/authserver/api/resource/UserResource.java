package com.myroom.authserver.api.resource;

import com.google.firebase.auth.FirebaseAuthException;
import com.myroom.authserver.api.constants.ApiConstants;
import com.myroom.authserver.api.model.UserResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth User Resource")

@RestController
@RequestMapping(value = ApiConstants.AUTH_USER_API_V1)
public interface UserResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_UNAUTHORIZED),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_NOT_FOUND)
            }
    )
    @Operation(method = "GET", summary = "Get User Details")
    @GetMapping("/{uid}")
    ResponseEntity<UserResponseModel> getUser(
           @Parameter(name = "uid", description = "User uid")
           @Schema(description = "uid", example = "zwtWhso1vTeEhPC9iBLZM5KMfdF3", required = true)
           @PathVariable String uid
    ) throws FirebaseAuthException;
}