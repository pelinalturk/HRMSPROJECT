package kodlamaio.Hrms.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kodlamaio.Hrms.business.abstracts.CandidateService;
import kodlamaio.Hrms.business.abstracts.VerificationCodeService;
import kodlamaio.Hrms.core.utilities.result.ErrorResult;
import kodlamaio.Hrms.core.utilities.result.Result;
import kodlamaio.Hrms.core.utilities.result.SuccessResult;
import kodlamaio.Hrms.core.utilities.validation.EmailValidationManager;
import kodlamaio.Hrms.dataAccess.abstracts.CandidateDao;
import kodlamaio.Hrms.dataAccess.abstracts.UserDao;
import kodlamaio.Hrms.dataAccess.abstracts.VerificationCodeDao;
import kodlamaio.Hrms.entities.concretes.Candidate;

@Service
public class CandidateManager implements CandidateService{

	private CandidateDao candidateDao;
	private UserDao userDao;
	private VerificationCodeService verificationService;
	//private VerificationCodeDao codeDao;
	
	@Autowired
	public CandidateManager(CandidateDao candidateDao, UserDao userDao,VerificationCodeService verificationService) {
		super();
		this.candidateDao=candidateDao;
		this.userDao=userDao;
		this.verificationService= verificationService;
		//this.codeDao = codeDao;
	}
	@Override
	public List<Candidate> getall() {
		return this.candidateDao.findAll();
	}
	@Override
	public Result add(Candidate candidate) {
		if(this.userDao.findByEmail(candidate.getEmail()) != null) 
	    {
			return new ErrorResult("Bu mail adresi sistemde kayıtlıdır!");
		}
		else if(this.candidateDao.findByNationalIdentity(candidate.getNationalIdentity()) != null)
		{
			return new ErrorResult("Bu T.C. Kimlik numarası sistemde kayıtlıdır!");		
		}
		this.candidateDao.save(candidate);
		this.verificationService.add(candidate);
		
		return new SuccessResult("Doğrulama maili gönderildi.");
	}
}