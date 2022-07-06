package de.crafted.api.controller;

import de.crafted.api.controller.model.TicketInput;
import de.crafted.api.security.UserAgent;
import de.crafted.api.service.ticket.TicketService;
import de.crafted.api.service.ticket.model.TicketInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets/")
public class TicketController {
    private final TicketService ticketService;
    private final UserAgent userAgent;

    @Operation(summary = "Get ticket info.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content)
    })
    @GetMapping("/{ticketId}")
    public TicketInfo getTicketInfo(@PathVariable Long ticketId) {
        return ticketService.getTicketInfo(ticketId);
    }

    @Operation(summary = "Get all ticket infos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/")
    public List<TicketInfo> getTicketInfos() {
        return ticketService.getTicketInfos();
    }

    @Operation(summary = "Create new ticket.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/")
    public TicketInfo create(@RequestBody TicketInput ticketInput) {
        var user = userAgent.getUser();
        return ticketService.create(user.getId(), ticketInput);
    }

    @Operation(summary = "Update ticket.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content)
    })
    @PutMapping("/{ticketId}")
    public TicketInfo update(@PathVariable Long ticketId,
                             @RequestBody TicketInput ticketInput) {
        var user = userAgent.getUser();
        return ticketService.update(user.getId(), ticketId, ticketInput);
    }
}
