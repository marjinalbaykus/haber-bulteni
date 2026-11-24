# Görev: detay-14

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-14.json`
JSON dışında hiçbir şey yazma (açıklama, markdown kod bloğu yok). Dosyada başka bir şey olmasın.

## SİSTEM TALİMATI

You write the detailed Turkish version of ONE news story for a personal bulletin.
You get the one-line headline, the related news items (titles and snippets) and, when available,
extracted article texts that may be in English, Spanish, Chinese or Turkish.

Rules:
- Write natural, fluent Turkish. Translate foreign material.
- Use only facts in the provided material. If the material is thin, write a shorter text.
  Never invent numbers, quotes, dates or names.
- title: Turkish title, at most about 12 words.
- headline: the one-line Turkish bullet (at most about 25 words). Improve it if the articles give better
  facts, otherwise keep it.
- detail: 2-4 short paragraphs separated by a blank line. What happened, key numbers, context, and why
  it matters for this reader (topic criteria given).
- key_points: 2-5 short Turkish bullet points.
- Refer to sources by publisher name if needed, never by bracketed item numbers like [3].

Output JSON: {"title": str, "headline": str, "detail": str, "key_points": [str]}

## GİRDİ

TOPIC: Yapay Zeka Kodlama Araçları ve Fiyatlar

CRITERIA:
Kullanıcı Claude Code (Claude aboneliği ile) ve DeepSeek API'sini (OpenCode üzerinden ve bu bültenin
kendisinde deepseek-flash olarak) kullanıyor. İleride Claude'u mimar, DeepSeek'i işçi model olarak
kullanıp uygulama/oyun yazmayı planlıyor. Fiyat/performans en önemli ölçüt.
Önemli: Claude Code, OpenAI Codex, Cursor, OpenCode, Gemini CLI gibi kodlama araçlarında büyük
yenilikler; Claude abonelik planı, kullanım kotası veya fiyat değişiklikleri; DeepSeek'in yeni modelleri,
API fiyat değişiklikleri ve indirimli saat düzenlemeleri; kodlama testlerinde (SWE-bench vb.) büyük
sıçramalar ve fiyat/performans dengesini değiştiren yeni modeller; ucuz ama güçlü API seçenekleri.
Fiyat değişikliği varsa eski ve yeni fiyatı (1M token başına) yaz. Puan varsa testi ve puanı yaz.
Önemsiz: küçük sürüm notları, eklenti tanıtımları, "en iyi 10 yapay zeka kodlama aracı" listeleri,
kişisel deneyim yazıları, küçük girişimlerin araç duyuruları.

HEADLINE: Anthropic, Claude Ship'i duyurdu; AI ajanları kod değişikliklerini inceleyip düzeltebilecek ve pull request'leri onaylayabilecek.

RELATED ITEMS:
[881] 2026-11-23 23:29 | TestingCatalog AI News (en) | Anthropic launches Claude Ship to review and approve PRs
[688] 2026-11-23 21:31 | Crypto Briefing (en) | Anthropic launches Claude Ship to automate code reviews and approvals
[863] 2026-11-23 22:05 | TechCrunch (en) | Anthropic launches Claude Ship, letting AI agents review, fix, and approve code changes
[868] 2026-11-23 23:24 | NewsCord (en) | Anthropic Launches Claude Ship to Automate Code Reviews, Fixes, and PR Approvals: 11 outlets compared
[828] 2026-11-23 20:51 | Digital Trends (en) | Anthropic expands Claude Code’s review tools with automated fixes and approvals
[865] 2026-11-23 22:13 | www.trendingtopics.eu (en) | Claude Ship: Anthropic Now Lets Claude Approve Code, Too
[870] 2026-11-23 21:02 | citybiz (en) | Anthropic Launches Claude Ship to Automate Code Reviews, Fix Pull Requests and Accelerate Software Development
[897] 2026-11-23 21:00 | FourWeekMBA (en) | Anthropic’s Claude Ship Puts 3 Agents on Pull Requests
[871] 2026-11-23 20:55 | BigGo Finance (en) | Anthropic Debuts Claude Ship to Automate Pull Request Reviews and Merges
[885] 2026-11-23 20:55 | Meteora Web (en) | Claude Ship, Anthropic Automates Pull Requests
[878] 2026-11-23 20:47 | MLQ.ai (en) | Anthropic launches Claude Ship to review, fix and approve pull requests under team-set rules
[886] 2026-11-23 20:31 | Bradenton Herald (en) | Anthropic launches Claude Ship to help developers ship code faster
[867] 2026-11-23 20:24 | The New Stack (en) | Anthropic’s Claude Ship lets Claude approve routine pull requests

ARTICLE 1 (TechCrunch, en):
Anthropic is moving from having Claude write code to helping developers decide when that code is ready to ship. On Monday, the newly public AI company announced the launch of Claude Ship, a suite of AI agents for automating code reviews, fixes, and approvals of routine code changes.
The latter is the more interesting part of this new set of tools, as AI coding tools already offered code review features before now. What’s notable about Claude Ship is not just that it can review proposed code changes and identify and address problems, but that it also includes an agent that can approve code changes automatically based on policies set by a development team.
The launch arrives at a time when AI coding agents have become increasingly capable of generating substantial amounts of code, leading to bottlenecks in reviewing and approving the code changes before they go live. With Claude Ship, the idea is to automate even more of the work involved in the development process, essentially by delegating approval authority to an AI agent itself.
“Most teams want a person to approve every change, and that’s easy to keep up with when people write every line. But a lot of developers build with agents now,” a company blog post explains. “On the Claude Code team, Claude writes most of our code, and we wanted everything after the PR to move that fast too.”
Claude Ship is launching in beta, allowing early adopters to use it to review pull requests (PRs), and then decide what’s ready to merge.
The new offering consists of Code Review, which is already generally available, and two new agents now in beta: PR Steward and Claude Approvals. PR Steward focuses on addressing issues such as failing CI checks, merge conflicts, and feedback from Code Review. It also comments after every push on what it changed and how to roll it back, if needed.
Claude Approvals then applies an approval policy that the team writes as a file in the repository. This approval process can incorporate various signals, including Code Review findings or the company’s own classifiers, with policies written in natural language.
Because the offering is still in beta, Anthropic suggests running the approval functionality in observe-only mode first. Teams can also choose to start with just one repository and set monthly spending limits to have more control over the process.
Anthropic says teams will also still remain in control of where Claude Ship runs, which review settings to use (e.g., Quick, Standard, or Deep), and which changes should be escalated to a human for a decision.
Claude Ship is rolling out to Claude Team and Enterprise plans for repositories on GitHub. Code Review is generally available, while PR Steward and Claude Approvals are in beta.

ARTICLE 2 (Unite.AI, en):
AI Models & Platforms
Anthropic Launches Claude Ship to Review, Fix and Approve PRs
Add Unite.AI to your preferred sources on Google
Key PointsClaude Ship separates the power to fix pull requests from the authority to approve them.
View more
- Claude Ship separates the power to fix pull requests from the authority to approve them.
- Approval policies let routine changes pass while routing designated scenarios to deeper human sign-off.
- Anthropic reports merged pull requests more than doubled while the share reverted fell by more than 40%.
Anthropic launched Claude Ship on November 23, 2026, making the beta product available to Claude Team and Enterprise organizations for GitHub repositories. It pairs the generally available Code Review with two new public-beta agents, PR Steward and Claude Approvals, to carry pull requests through review, fixes and an approval decision.
Together, they address the handoffs after code is written. Code Review reads a change alongside the surrounding codebase, gathers and adversarially verifies findings, and posts severity-ranked inline comments on the affected lines. PR Steward can push commits containing fixes, while Claude Approvals checks the pull request against a policy written by the team.
Review depth is configurable. Administrators can select Quick, Standard or Deep review levels for each repository and choose when reviews run, allowing different repositories to use different levels of scrutiny and automation.
PR Steward Pushes Fixes Without Merge Authority
PR Steward responds to failing continuous-integration checks, merge conflicts and feedback from Code Review, other review tools or team members. After every push, it comments on what changed and how to undo the work. Developers can steer the agent with an @claude instruction or tell it to step back with @claude stop.
PR Steward is designed not to approve or merge pull requests. Repository settings and GitHub branch rules determine what can merge, keeping the authority to complete a pull request separate from the agent’s ability to commit fixes.
Approval Policies Determine Human Sign-Off
Claude Approvals uses a policy file that the team writes in the repository. The policy can set how many people must approve changes to a path and where Claude may post an approving review. Routine changes can receive Claude’s approval when permitted, while designated scenarios require deeper human sign-off. Decision signals can include Code Review findings and the team’s own classifiers written in natural language.
Anthropic recommends that most customers begin in observe-only mode, where the check reports what the policy would decide and normally does not block merging. Enforcement requires the organization to make Claude Approvals a required GitHub check.
The technical documentation warns both that GitHub auto-merge or a merge queue can merge a change without anyone acting if the policy does not require a person’s approval, and that Claude Ship may miss bugs, security vulnerabilities or other issues.
Rollout, Early Results and Token Billing
Developers can start the process on their own changes with /ship, which began rolling out in Claude Code and the desktop app alongside the launch. The command pushes the change, opens a pull request, reviews it and works on feedback and failing checks, subject to the developer’s permissions and repository branch rules. Organizations can instead enable Claude Ship repository by repository so its agents start across team pull requests without a manual /ship invocation.
Anthropic runs Claude Ship on many of its own pull requests. On the Claude Code team, where Claude’s review is one layer of a broader process, the company reports that merged pull requests more than doubled from June to September 2026 while the share reverted fell by more than 40%.
Claude Ship usage is billed by tokens separately from a plan’s included usage. Costs depend on the number and size of pull requests, review level and frequency, and the amount of PR Steward work. Administrators can set spend limits for Code Review and PR Steward. Claude Approvals has no base charge, but classifier runs consume billed tokens and a policy-requested Claude review triggers a billed Code Review even in observe mode. Running /ship counts as regular Claude Code usage.
Eligible Claude Enterprise organizations can claim at least $10,000 in Claude Ship credits by January 15, 2027, for use within 30 days of claiming. The agents are unavailable to organizations using Zero Data Retention, the HIPAA configuration or customer-managed encryption keys.
Claude Ship moves Anthropic’s agents beyond producing code and into coordinating the work that surrounds a pull request. Its practical role is defined by those repository controls: policy, required checks and branch rules determine whether it assists human reviewers or allows changes to merge without human involvement.

ARTICLE 3 (MarkTechPost, en):
Anthropic just released Claude Ship. Claude Ship is a set of Anthropic agents that takes GitHub pull requests from review to approval. It bundles 3 agents: Code Review, PR Steward and Claude Approvals. Your GitHub branch rules still decide what merges.
TL;DR
- Size: 3 agents with a local /ship command.
- Runs on: Anthropic’s cloud for Code Review and PR Steward. /ship runs in the Claude Code CLI locally.
- Performance: Most Code Reviews finish in 6 to 10 minutes, about 3x faster than the research preview.
- Best: Claude Code team merged PRs more than doubled from June to September (self-reported).
What is Claude Ship?
Per the Claude Ship docs, each agent owns 1 step:
- Code Review looks for logic errors, security vulnerabilities and broken edge cases. Findings land as inline comments, ranked by severity.
- PR Steward works on failing checks, merge conflicts and review comments. It pushes commits with its fixes.
- Claude Approvals checks each PR against an approval policy your team writes.
Code Review is generally available. The other 2 are in public beta.
How does Claude Ship work on a pull request?
Code Review finds problems. PR Steward pushes fixes, and a fix can start another review. Claude Approvals then applies your policy. The merge follows your GitHub branch rules.
PR Steward is designed not to approve or merge. After each push, it comments on its changes and how to undo them. Developers steer it with @claude and stop it with @claude stop.
Claude Approvals reads a policy file at .claude/approvals.yml. Anthropic calls it a smarter CODEOWNERS file. A rule can require 2 people on billing code and 1 on test fixtures. Rules can read PR facts and classifier answers, but not CI results.
What does /ship do for developers?
Developers run /ship in Claude Code. It commits, pushes and opens the PR. Claude reviews it, then ‘watches’ feedback and failing checks. You merge yourself.
Requirements are Claude Code v2.1.330 or later, GitHub CLI 2.63 or later, and git 2.38 or later. A watch ends after 7 days, or on merge or close. /steward skips the review on an open PR.
When Claude acts alone, Claude Code blocks merge, approve, auto-merge and force-push commands. Still, a watch uses your own gh and git sign-ins.
Claude Ship benchmarks: what has Anthropic reported?
Anthropic has published no public benchmark. It shares internal results from its launch post and engineering blog:
- Merged PRs on the Claude Code team more than doubled from June to September.
- The share of reverted changes fell by more than 40% over the same period.
- Internal PRs with substantive review comments rose from 16% to 54%.
- PRs passing after 1 review-and-fix round rose from 65% to 78%.
- Mean reviews needed to pass fell from 2.08 to 1.32.
These numbers are self-reported by Anthropic. No precision or recall is reported.
How does Claude Ship compare with Copilot, Bugbot and CodeRabbit?
Competitor figures come from each vendor’s own docs, checked November 23, 2026.
Claude Ship pricing, deployment and availability
Claude Ship is for Claude Team and Enterprise plans on GitHub. Owners enable agents per repository, ideally 1 at a time.
Billing runs on tokens through usage credits, separate from plan usage. Code Review costs scale with PR count, size, review level and frequency. Admins can set spend limits. PR Steward has none until an Owner sets one.
Approvals has no base charge. Classifier runs and the reviews a policy requests are billed, even in observe mode. Running /ship counts as regular Claude Code usage.
Eligible Enterprise organizations can claim at least $10,000 in Claude Ship credits by January 15, 2027. Credits expire 30 days after claiming, per the credit terms.
Key Takeaways
- Claude Ship bundles 3 GitHub agents: Code Review, PR Steward and Claude Approvals.
- Code Review is GA; PR Steward and Approvals are in public beta.
- Approval rules live in .claude/approvals.yml; start in observe mode.
- Anthropic reports over 2x merged PRs and over 40% fewer reverts (self-reported).
- Token billing, Team and Enterprise only, no ZDR, HIPAA or CMEK.
Check out the launch post, the Claude Ship docs and the /ship command docs. You can check the deployment options and specs here through our GPULeaderboard.com chrome extension.
Also, feel free to follow us on Twitter and don’t forget to join our 150k+ML SubReddit and Subscribe to our Newsletter. Wait! are you on telegram? now you can join us on telegram as well.
Need to partner with us for promoting your GitHub Repo OR Hugging Face Page OR Product Release OR Webinar etc.? Connect with us
Asif Razzaq is the CEO of Marktechpost AI Media Inc.. As a visionary entrepreneur and engineer, Asif is committed to harnessing the potential of Artificial Intelligence for social good. His most recent endeavor is the launch of an Artificial Intelligence Media Platform, Marktechpost, which stands out for its in-depth coverage of machine learning and deep learning news that is both technically sound and easily understandable by a wide audience. The platform boasts of over 2 million monthly views, illustrating its popularity among audiences.
