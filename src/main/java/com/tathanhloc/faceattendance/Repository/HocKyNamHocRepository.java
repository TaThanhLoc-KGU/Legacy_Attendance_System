package com.tathanhloc.faceattendance.Repository;

import com.tathanhloc.faceattendance.Model.HocKyNamHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HocKyNamHocRepository extends JpaRepository<HocKyNamHoc, Integer> {

    /**
     * Tìm tất cả relationship theo mã năm học
     */
    List<HocKyNamHoc> findByNamHoc_MaNamHoc(String maNamHoc);

    /**
     * Tìm tất cả relationship đang hoạt động theo mã năm học
     */
    List<HocKyNamHoc> findByNamHoc_MaNamHocAndIsActive(String maNamHoc, Boolean isActive);

    /**
     * Tìm tất cả relationship theo mã học kỳ
     */
    List<HocKyNamHoc> findByHocKy_MaHocKy(String maHocKy);

    /**
     * Tìm tất cả relationship đang hoạt động theo mã học kỳ
     */
    List<HocKyNamHoc> findByHocKy_MaHocKyAndIsActive(String maHocKy, Boolean isActive);

    /**
     * Tìm relationship cụ thể giữa học kỳ và năm học
     */
    Optional<HocKyNamHoc> findByHocKy_MaHocKyAndNamHoc_MaNamHoc(String maHocKy, String maNamHoc);

    /**
     * Kiểm tra tồn tại relationship
     */
    boolean existsByHocKy_MaHocKyAndNamHoc_MaNamHoc(String maHocKy, String maNamHoc);

    /**
     * Đếm số lượng học kỳ của một năm học
     */
    @Query("SELECT COUNT(h) FROM HocKyNamHoc h WHERE h.namHoc.maNamHoc = :maNamHoc AND h.isActive = true")
    long countActiveSemestersByYear(@Param("maNamHoc") String maNamHoc);

    /**
     * Lấy danh sách học kỳ theo năm học với thông tin chi tiết
     */
    @Query("SELECT h FROM HocKyNamHoc h " +
            "JOIN FETCH h.hocKy hk " +
            "JOIN FETCH h.namHoc nh " +
            "WHERE h.namHoc.maNamHoc = :maNamHoc AND h.isActive = true " +
            "ORDER BY hk.ngayBatDau ASC")
    List<HocKyNamHoc> findSemestersWithDetailsByYear(@Param("maNamHoc") String maNamHoc);

    /**
     * Lấy danh sách năm học theo học kỳ với thông tin chi tiết
     */
    @Query("SELECT h FROM HocKyNamHoc h " +
            "JOIN FETCH h.hocKy hk " +
            "JOIN FETCH h.namHoc nh " +
            "WHERE h.hocKy.maHocKy = :maHocKy AND h.isActive = true")
    List<HocKyNamHoc> findYearsWithDetailsBySemester(@Param("maHocKy") String maHocKy);

    /**
     * Xóa mềm tất cả relationship của một năm học
     */
    @Query("UPDATE HocKyNamHoc h SET h.isActive = false WHERE h.namHoc.maNamHoc = :maNamHoc")
    void softDeleteByYear(@Param("maNamHoc") String maNamHoc);

    /**
     * Xóa mềm tất cả relationship của một học kỳ
     */
    @Query("UPDATE HocKyNamHoc h SET h.isActive = false WHERE h.hocKy.maHocKy = :maHocKy")
    void softDeleteBySemester(@Param("maHocKy") String maHocKy);

    /**
     * Khôi phục tất cả relationship của một năm học
     */
    @Query("UPDATE HocKyNamHoc h SET h.isActive = true WHERE h.namHoc.maNamHoc = :maNamHoc")
    void restoreByYear(@Param("maNamHoc") String maNamHoc);

    /**
     * Tìm tất cả relationship đang hoạt động
     */
    List<HocKyNamHoc> findByIsActive(Boolean isActive);


    // Tìm học kỳ theo năm học
    List<HocKyNamHoc> findByNamHocMaNamHocAndIsActiveTrue(String maNamHoc);

    // Tìm năm học theo học kỳ
    Optional<HocKyNamHoc> findByHocKyMaHocKyAndIsActiveTrue(String maHocKy);

    // Tìm học kỳ theo thứ tự trong năm học
    Optional<HocKyNamHoc> findByNamHocMaNamHocAndThuTuAndIsActiveTrue(String maNamHoc, Integer thuTu);

    // Đếm số học kỳ trong năm học
    @Query("SELECT COUNT(h) FROM HocKyNamHoc h WHERE h.namHoc.maNamHoc = :maNamHoc AND h.isActive = true")
    Long countByNamHoc(@Param("maNamHoc") String maNamHoc);

    // Tìm tất cả học kỳ có sắp xếp theo thứ tự
    @Query("SELECT h FROM HocKyNamHoc h WHERE h.namHoc.maNamHoc = :maNamHoc AND h.isActive = true ORDER BY h.thuTu")
    List<HocKyNamHoc> findByNamHocOrderByThuTu(@Param("maNamHoc") String maNamHoc);

    /**
     * Đếm số relationships theo học kỳ
     */
    long countByHocKy_MaHocKyAndIsActive(String maHocKy, Boolean isActive);

    /**
     * Khôi phục relationships theo học kỳ
     */
    @Modifying
    @Query("UPDATE HocKyNamHoc h SET h.isActive = true WHERE h.hocKy.maHocKy = :maHocKy")
    void restoreBySemester(@Param("maHocKy") String maHocKy);

    /**
     * Tìm relationships theo trạng thái hoạt động của năm học
     */
    @Query("SELECT h FROM HocKyNamHoc h JOIN h.namHoc nh WHERE nh.isActive = :isActive")
    List<HocKyNamHoc> findByNamHocIsActive(@Param("isActive") Boolean isActive);

    /**
     * Tìm relationships theo trạng thái hoạt động của học kỳ
     */
    @Query("SELECT h FROM HocKyNamHoc h JOIN h.hocKy hk WHERE hk.isActive = :isActive")
    List<HocKyNamHoc> findByHocKyIsActive(@Param("isActive") Boolean isActive);

}