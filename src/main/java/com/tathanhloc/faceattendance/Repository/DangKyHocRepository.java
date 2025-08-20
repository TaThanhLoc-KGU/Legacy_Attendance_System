package com.tathanhloc.faceattendance.Repository;

import com.tathanhloc.faceattendance.Model.DangKyHoc;
import com.tathanhloc.faceattendance.Model.DangKyHocId;
import com.tathanhloc.faceattendance.Model.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DangKyHocRepository extends JpaRepository<DangKyHoc, DangKyHocId> {
    List<DangKyHoc> findBySinhVien_MaSv(String maSv);
    List<DangKyHoc> findByLopHocPhan_MaLhp(String maLhp);

    /**
     * Tìm đăng ký học theo lớp học phần
     */
    List<DangKyHoc> findByLopHocPhanMaLhp(String maLhp);

    /**
     * Đếm số sinh viên trong lớp học phần
     */
    long countByLopHocPhanMaLhp(String maLhp);

    /**
     * Kiểm tra sinh viên có đăng ký lớp không
     */
    boolean existsByLopHocPhanMaLhpAndSinhVienMaSv(String maLhp, String maSv);
    boolean existsByIdMaSvAndIdMaLhpAndIsActiveTrue(String maSv, String maLhp);

    // THÊM CÁC METHOD VALIDATION MỚI
    /**
     * Kiểm tra sinh viên đã đăng ký môn học này (bất kỳ nhóm nào) chưa
     */
    @Query("SELECT d FROM DangKyHoc d " +
            "JOIN d.lopHocPhan lhp " +
            "WHERE d.sinhVien.maSv = :maSv " +
            "AND lhp.monHoc.maMh = :maMh " +
            "AND d.isActive = true")
    List<DangKyHoc> findBySinhVienAndMonHoc(@Param("maSv") String maSv, @Param("maMh") String maMh);

    /**
     * Kiểm tra sinh viên có đăng ký môn học này chưa
     */
    @Query("SELECT COUNT(d) > 0 FROM DangKyHoc d " +
            "JOIN d.lopHocPhan lhp " +
            "WHERE d.sinhVien.maSv = :maSv " +
            "AND lhp.monHoc.maMh = :maMh " +
            "AND d.isActive = true")
    boolean existsBySinhVienAndMonHoc(@Param("maSv") String maSv, @Param("maMh") String maMh);

    /**
     * Tìm lớp học phần hiện tại của sinh viên trong môn học
     */
    @Query("SELECT d.lopHocPhan.maLhp FROM DangKyHoc d " +
            "JOIN d.lopHocPhan lhp " +
            "WHERE d.sinhVien.maSv = :maSv " +
            "AND lhp.monHoc.maMh = :maMh " +
            "AND d.isActive = true")
    Optional<String> findCurrentLhpByStudentAndSubject(@Param("maSv") String maSv, @Param("maMh") String maMh);

    @Query("SELECT lsv.sinhVien FROM DangKyHoc lsv WHERE lsv.lopHocPhan.maLhp = :maLhp AND lsv.isActive = true")
    List<SinhVien> findStudentsByClass(@Param("maLhp") String maLhp);
}