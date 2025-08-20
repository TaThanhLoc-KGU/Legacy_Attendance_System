package com.tathanhloc.faceattendance.Controller;

import com.tathanhloc.faceattendance.DTO.GiangVienDTO;
import com.tathanhloc.faceattendance.Service.GiangVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/giangvien")
@RequiredArgsConstructor
public class GiangVienController {

    private final GiangVienService giangVienService;

    @GetMapping("/{id}")
    public GiangVienDTO getById(@PathVariable String id) {
        return giangVienService.getById(id);
    }

    @PostMapping
    public GiangVienDTO create(@RequestBody GiangVienDTO dto) {
        return giangVienService.create(dto);
    }

    @PutMapping("/{id}")
    public GiangVienDTO update(@PathVariable String id, @RequestBody GiangVienDTO dto) {
        return giangVienService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        giangVienService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-magv/{maGv}")
    public ResponseEntity<GiangVienDTO> getByMaGv(@PathVariable String maGv) {
        return ResponseEntity.ok(giangVienService.getByMaGv(maGv));
    }

    // Thêm các endpoints sau vào GiangVienController.java hiện tại:

    /**
     * Lấy danh sách với filter
     */
    @GetMapping
    public ResponseEntity<List<GiangVienDTO>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String khoa
    ) {
        List<GiangVienDTO> result;

        // Lọc theo trạng thái
        if ("active".equals(status)) {
            result = giangVienService.getAllActive();
        } else if ("inactive".equals(status)) {
            result = giangVienService.getAllInactive();
        } else {
            result = giangVienService.getAll();
        }

        // Lọc theo tìm kiếm
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            result = result.stream()
                    .filter(gv ->
                            gv.getMaGv().toLowerCase().contains(searchLower) ||
                                    gv.getHoTen().toLowerCase().contains(searchLower) ||
                                    gv.getEmail().toLowerCase().contains(searchLower)
                    )
                    .collect(Collectors.toList());
        }

        // Lọc theo khoa
        if (khoa != null && !khoa.trim().isEmpty()) {
            result = result.stream()
                    .filter(gv -> gv.getMaKhoa().equals(khoa))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(result);
    }

    /**
     * Khôi phục giảng viên
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable String id) {
        giangVienService.restore(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Lấy thống kê
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLecturers", giangVienService.getAll().size());
        stats.put("activeLecturers", giangVienService.countByStatus(true));
        stats.put("inactiveLecturers", giangVienService.countByStatus(false));
        return ResponseEntity.ok(stats);
    }

    // Thêm vào GiangVienController.java ngay sau @RequestMapping("/api/giangvien")

    /**
     * Lấy danh sách giảng viên đang hoạt động
     */
    @GetMapping("/active")
    public ResponseEntity<List<GiangVienDTO>> getAllActive() {
        try {
            List<GiangVienDTO> activeGiangVien = giangVienService.getAllActive();
            return ResponseEntity.ok(activeGiangVien);
        } catch (Exception e) {
            System.err.println("❌ Error getting active lecturer list: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    /**
     * Lấy danh sách giảng viên không hoạt động
     */
    @GetMapping("/inactive")
    public ResponseEntity<List<GiangVienDTO>> getAllInactive() {
        try {
            List<GiangVienDTO> inactiveGiangVien = giangVienService.getAllInactive();
            return ResponseEntity.ok(inactiveGiangVien);
        } catch (Exception e) {
            System.err.println("❌ Error getting inactive lecturer list: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getTeacherCount() {
        try {
            long count = giangVienService.count();
            Map<String, Object> response = new HashMap<>();
            response.put("count", count);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("count", 0);
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

}
